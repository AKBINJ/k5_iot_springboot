package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.common.enums.OrderStatus;
import com.example.k5_iot_springboot.dto.I_Order.request.OrderRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.OrderResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.List;

public interface I_OrderService {
    ResponseDto<OrderResponse.Detail> create(UserPrincipal userPrincipal, OrderRequest.OrderCreateRequest req);

    ResponseDto<OrderResponse.Detail> approve(UserPrincipal userPrincipal, Long orderId);

    ResponseDto<OrderResponse.Detail> cancel(UserPrincipal userPrincipal, Long orderId);


    ResponseDto<List<OrderResponse.Detail>> search(UserPrincipal userPrincipal, Long userId, OrderStatus status, LocalDateTime from, LocalDateTime to);
}
