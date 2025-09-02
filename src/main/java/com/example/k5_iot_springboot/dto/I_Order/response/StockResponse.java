package com.example.k5_iot_springboot.dto.I_Order.response;

public class StockResponse {
    /** 재고 응답 DTO */
    public record Response(
            Long productId,
            int quantity
    ) {}
}
