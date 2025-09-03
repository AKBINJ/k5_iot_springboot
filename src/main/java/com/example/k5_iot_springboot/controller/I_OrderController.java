package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.common.enums.OrderStatus;
import com.example.k5_iot_springboot.dto.I_Order.request.OrderRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.OrderResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.I_OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// === Controller 기본 어노테이션 === //
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
/**
 * 주문 생성/승인/취소 + 검색
 * */
public class I_OrderController {
    private final I_OrderService orderService; // 생성자 주입

    /** 주문 생성: 인증 주체의 userId를 사용 (Post: 전송하다 - 새로운 데이터 생성) */
    @PostMapping
    // 접근제어자 반환타입 메서드명() {}
    // cf) ResponseEntity(HttpStatus 상태코드, HttpHeaders 요청/응답에 대한 요구사항, HttpBody 응답 본문 )
    //     ResponseDto(HttpBody 응답 본문 타입) - 데이터 전송 객체
    //          >> result(boolean), message(String), data(T): 실제 요청 데이터 타입
    public ResponseEntity<ResponseDto<OrderResponse.Detail>> create(
            // 매개변수 - Controller (@PathVariable, @RequestBody, @RequestParam)
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody OrderRequest.OrderCreateRequest req
    ) {
       ResponseDto<OrderResponse.Detail> response = orderService.create(userPrincipal, req);
//       return ResponseEntity.ok(response);
       return ResponseEntity.ok().body(response);
    }

    /** 주문 승인: USER 불가, ADMIN/MANAGER만 가능 */
    @PostMapping("/{orderId}/approve")
    public ResponseEntity<ResponseDto<OrderResponse.Detail>> approve(
            @AuthenticationPrincipal UserPrincipal userPrincipal, // 주문 승인자 정보르 저장(활용)할 경우
            @PathVariable Long orderId
    ) {
        ResponseDto<OrderResponse.Detail> response = orderService.approve(userPrincipal, orderId);
        return ResponseEntity.ok(response);
    }

    /** 주문 취소: USER(본인 + PENDING 한정), MANAGER, ADMIN */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseDto<OrderResponse.Detail>> cancel(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long orderId
    ) {
        ResponseDto<OrderResponse.Detail> response = orderService.cancel(userPrincipal, orderId);
        return ResponseEntity.ok(response);
    }

    /** 주문 검색: USER(본인), MANGER, ADMIN */
    @GetMapping
    public ResponseEntity<ResponseDto<List<OrderResponse.Detail>>> search(
            @AuthenticationPrincipal UserPrincipal userPrincipal,       // 로그인한 사용자 정보
            @RequestParam(required = false) Long userId,                // 검색할 사용자 정보
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to
            ) {
        ResponseDto<List<OrderResponse.Detail>> response = orderService.search(userPrincipal, userId, status, from, to);
        return ResponseEntity.ok(response);
    }
}
