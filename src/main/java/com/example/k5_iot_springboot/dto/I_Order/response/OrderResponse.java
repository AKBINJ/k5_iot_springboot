package com.example.k5_iot_springboot.dto.I_Order.response;

import com.example.k5_iot_springboot.common.enums.OrderStatus;

import java.util.List;

public class OrderResponse {
    /** 주문 상세 응답 DTO */
    public record Detail(
            Long orderId,
            Long userId,
            OrderStatus status,
            Integer totalAmount,
            Integer totalQuantity,
            String createdAt,
            List<OrderItemList> items

    ) {}

    /** 주문 항목 리스트 응답 DTO */
    public record OrderItemList(
            Long productId,
            String productName,
            Integer price,
            Integer quantity,
            Integer lineTotal
    ) {}
}
