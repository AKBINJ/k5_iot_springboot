package com.example.k5_iot_springboot.dto.I_Order.request;

import com.example.k5_iot_springboot.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {
    /** 주문 생성 요청 DTO */
    public record OrderCreateRequest(
            List<OrderItemLine> items
    ) {}

    /** 주문 항목(라인) - 생성 요청용
     * int(기본형) VS Integer(참조형)
     * 1) int
     *      : null 불가 (값이 무조건 존재해야 함)
     *      - 주로 필수값일 때 사용
     *      - 계산에 직접 사용 가능(사칙연산 등)
     *
     * 2) Integer
     *      : null 가능 (값이 없다는 상태 표현 가능)
     *      - JPA/Hibernate 매핑 시 DB에서 NULL 그대로 받기 위해 주로 사용
     *      - 제네릭 컬렉션(List, Map)에는 무조건 객체 타입
     *      예 뷰에서 집계 결과가 없으면 NULL 가능 - order_total_qty
     *
     * cf) 수량(quantity) - int
     *     합계(totalQuantity) - Integer
     * */
    public record OrderItemLine(
            Long productId,
            int quantity
    ) {}

    /** 주문 조회 조건 DTO */
    public record OrderSearchCondition(
            Long userId,
            OrderStatus status,
            LocalDateTime from,
            LocalDateTime to
    ) {}
}
