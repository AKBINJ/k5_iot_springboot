package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.common.enums.OrderStatus;
import com.example.k5_iot_springboot.common.utils.DateUtils;
import com.example.k5_iot_springboot.dto.I_Order.request.OrderRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.OrderResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.*;
import com.example.k5_iot_springboot.repository.I_OrderRepository;
import com.example.k5_iot_springboot.repository.I_ProductRepository;
import com.example.k5_iot_springboot.repository.I_StockRepository;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.I_OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 인터페이스의 추상 메서드를 Impl 클래스 파일에서 "강제 구현"
@Service
@RequiredArgsConstructor // final 필드 OR @NonNull 필드만을 매개변수로 가지는 생성자
@Transactional(readOnly = true)
public class I_OrderServiceImpl implements I_OrderService {
    private final EntityManager em; // 상용자 참조 - getReference 등
    private final I_OrderRepository orderRepository;
    private final I_ProductRepository productRepository;
    private final I_StockRepository stockRepository;

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<OrderResponse.Detail> create(UserPrincipal userPrincipal, OrderRequest.OrderCreateRequest req) {
        OrderResponse.Detail data = null;

        if (req.items() == null || req.items().isEmpty())
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");

        // principal에서 userId 추출
        Long authUserId = userPrincipal.getId();

        // EntityManager.getReference() VS JPA.findById()
        // 1) EntityManager.getReference()
        //      : 단순히 연관관계 주입만 필요할 때 사용
        //      - 실제 SQL SELECT문을 실행하지 않고, 프록시 객체를 반환
        //      >> 어차피 Order 엔티티의 user를 참조하는데 실제 User의 다른 필드가 필요없는 경우 효율적
        // 2) UserRepository.findById()
        //      : DB 조회 쿼리를 날리고 G_User 엔티티를 반환
        //      >> 존재하지 않는 userId이면 예외를 던지고 싶다 (안전성)

        // 인증 주체 authUserId로 G_User 프록시(대리인, 중계자) 획득 (UserRepository 없이도 가능)
        G_User userRef = em.getReference(G_User.class, authUserId);

        I_Order order = I_Order.builder()
                .user(userRef)
                .orderStatus(OrderStatus.PENDING) //기본값 - PENDING
                .build();

        for (OrderRequest.OrderItemLine line: req.items()) { // List<OrderItemLine> items
            if (line.quantity() <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
            I_Product product = productRepository.findById(line.productId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. id=" + line.productId()));
            I_OrderItem item = I_OrderItem.builder()
                    .product(product)
                    .quantity(line.quantity())
                    .build();
            order.addItem(item);
        }

        I_Order saved = orderRepository.save(order);

        data = toOrderResponse(saved);

        return ResponseDto.setSuccess("주문이 성공적으로 등록되었습니다.", data);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseDto<OrderResponse.Detail> approve(UserPrincipal userPrincipal, Long orderId) {
        OrderResponse.Detail data = null;

        I_Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(()-> new EntityNotFoundException("주문을 찾을 수 없습니다. id=" + orderId));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("PENDING 상태만 승인할 수 있습니다.");
        }

        // Map 컬렉션 프레임워크 사용
        // 주문 항목: 상품 A X 2 / 상품 B X 3 / 상품 A X 3
        //      >> 단순히 리스트로 순회하며 차감 시 상품 A 재고를 두 번 차감
        //      - Map<Long, Integer>: key=productId, value=누적수량 (수량을 합하여 한 번 차감/복원)
        Map<Long, Integer> needMap = new HashMap<>();
        order.getItems().forEach(item -> needMap.merge(
                item.getProduct().getId(),      //key
                item.getQuantity(),             //value
                Integer::sum));                 // key를 기준으로 동리한 Integer 값 합계

        // 재고 확인 & 차감 (productId 단위로 처리)
        for (Map.Entry<Long, Integer> e: needMap.entrySet()) {
            Long productId = e.getKey();
            int need = e.getValue();
            I_Stock stock = stockRepository.findByProductIdForUpdate(productId)
                    .orElseThrow(() -> new IllegalArgumentException("재고 정보가 없습니다. id=" + productId));
            if (stock.getQuantity() < need)
                throw new IllegalStateException("재고 부족: productId=%d, 필요=%d, 보유=%d".formatted(productId, need, stock.getQuantity()));
            stock.setQuantity(stock.getQuantity() - need);
        }
        order.setOrderStatus(OrderStatus.APPROVED);
        // 상태 변경 트리거가 order_logs 자동 기록

        data = toOrderResponse(order);

        return ResponseDto.setSuccess("주문이 정상적으로 승인되었습니다.", data);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or @authz.canCancel(#orderId, authentication)")
    public ResponseDto<OrderResponse.Detail> cancel(UserPrincipal userPrincipal, Long orderId) {
        OrderResponse.Detail data = null;
        I_Order order = orderRepository.findDetailById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. id=" + orderId));

        // 이미 취소된 주문일 경우 그대로 반환 (또는 예외 발생
        if (order.getOrderStatus() == OrderStatus.CANCELLED){
            throw  new IllegalStateException("이미 취소된 주문입니다.");
            // return ResponseDto.setFailed("이미 취소된 주문입니다.")
        }

        // == MANGER와 ADMIN은 PENDING 상태가 아니어도 (APPROVED 상태라도) 취소 가능 == //
        // 상태별 분기
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            // 권한 확인 필요 X
            // +) 재고 차감이 없었기 때문에 복원 불필요
            order.setOrderStatus(OrderStatus.CANCELLED);
        } else if (order.getOrderStatus() == OrderStatus.APPROVED) {
            // 승인 후(APPROVED): 권한 확인 필요 O
            // +) MANAGER/ADMIN만 취소 허용
            // +) 재고 복원 수정
            if (!hasManagerOrAdmin(userPrincipal)) {
                // hasManagerOrAdmin의 결과값이 false인 경우
                throw new IllegalArgumentException("승인된 주문는 관리자 권한(MANAGER/ADMIN)만 취소할 수 있습니다.");
            }
            // 권한이 MANAGER / ADMIN 인 경우
            // : 재고 복원
            Map<Long, Integer> restoreMap = new HashMap<>();
            // 같은 상품 정보에 수량에 대한 중복 제거 (단일 수량으로 합치는 기능)
            for (I_OrderItem item:  order.getItems()) {
                Long productId = item.getProduct().getId();         // 해당 주문 항목의 상품 고유 ID를 순회하여 저장
                int quantity = item.getQuantity();                  // 상품 주문 항목의 주문 수량 순회하여 저장
                Integer prev = restoreMap.get(productId);           // 상품 ID를 Key로 하고, 수량을 value로 저장하는 Map
                                                                    //  >> 현재 productId에 해당하는 기존 수량을 가져옴
                                                                    //      , 해당 Key가 없다면 null 반환
                restoreMap.put(productId, (prev == null ? quantity : prev + quantity));
            }

            // 중복없는 구매의 제품 Id에 대해 재고를 복구
            for (Map.Entry<Long, Integer> e: restoreMap.entrySet()) {
                Long productId = e.getKey();
                int quantity = e.getValue();

                // 재고 레코드 행 단위 잠금
                I_Stock stock = stockRepository.findByProductIdForUpdate(productId)
                        .orElseThrow(() -> new IllegalStateException("재고 정보가 없습니다. productId=" + productId));

                stock.setQuantity(stock.getQuantity() - quantity); // 변경 감지로 UPDATE
            }
            order.setOrderStatus(OrderStatus.CANCELLED);
        } else {
            throw new IllegalArgumentException("취소할 수 업슨ㄴ 주문 상태입니다: "+ order.getOrderStatus());
        }

        // PENDING일 때만 취소 가능한 로직
        // PENDING이 아니면 취소 불가
//        if (order.getOrderStatus() != OrderStatus.PENDING) {
//            throw new IllegalArgumentException("PENDING 상태의 주문만 취소할 수 있습니다,");
//        }
//
//        order.setOrderStatus(OrderStatus.CANCELLED);

        // + 변경 정보 자동 저장
        // + 변경 발생 시 DB 트리거에 의해 로그 기록 생성

        data = toOrderResponse(order);
        return ResponseDto.setSuccess("주문 취소가 정상적으로 진행되었습니다.", data);
    }

    @Override
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or @authz.isSelf(#userId, authentication)")
    public ResponseDto<List<OrderResponse.Detail>> search(UserPrincipal userPrincipal, Long userId, OrderStatus status, LocalDateTime from, LocalDateTime to) {
        List<OrderResponse.Detail> data = null;

        LocalDateTime fromUtc = DateUtils.kstToUtc(from);
        LocalDateTime toUtc = DateUtils.kstToUtc(to);

        List<I_Order> orders = orderRepository.searchOrders(userId, status, fromUtc, toUtc);

        data = orders.stream()
                .map(this::toOrderResponse)
                .toList();

        return ResponseDto.setSuccess("조건 검색이 정상적으로 진행되었습니다.", data);
    }

    // === 변환 유틸 === //
    private OrderResponse.Detail toOrderResponse(I_Order order) {
        // 각 주문 항목 변환
        List<OrderResponse.OrderItemList> items = order.getItems().stream()
                .map(item -> {
                    int price = item.getProduct().getPrice();
                    int quantity = item.getQuantity();
                    int lineTotal = (int) price * quantity;

                    return new OrderResponse.OrderItemList(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            price,
                            quantity,
                            lineTotal
                    );
                }).toList();

        // 총액 계산 (long)
        int totalAmount = items.stream()
                .mapToInt(OrderResponse.OrderItemList::lineTotal)
                .sum();

        // 총 수량 계산
        int totalQuantity = items.stream()
                .mapToInt(OrderResponse.OrderItemList::quantity)
                .sum();

        return new OrderResponse.Detail(
                order.getId(),
                order.getUser().getId(),
                order.getOrderStatus(),
                totalAmount,
                totalQuantity,
                DateUtils.toKstString(order.getCreatedAt()),
                items
        );
    }

    // == 호출자 권한이 MANAGER/ADMIN인지 확인 == //
    private boolean hasManagerOrAdmin(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getAuthorities() == null) return false;

        for (GrantedAuthority auth: userPrincipal.getAuthorities()) {
            String role = auth.getAuthority();
            if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) return true;
        }
        return false;
    }
}


