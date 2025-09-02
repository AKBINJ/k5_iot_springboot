package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.dto.I_Order.request.StockRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.StockResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import jakarta.validation.Valid;

public interface I_StockService {
    ResponseDto<StockResponse.Response> adjust(UserPrincipal userPrincipal, StockRequest.@Valid StockAdjust req);

    ResponseDto<StockResponse.Response> set(UserPrincipal userPrincipal, StockRequest.@Valid StockSet req);

    ResponseDto<StockResponse.Response> get(Long productId);

}
