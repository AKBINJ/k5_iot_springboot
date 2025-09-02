package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.common.constants.ApiMappingPattern;
import com.example.k5_iot_springboot.dto.I_Order.request.StockRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.StockResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.I_StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

/**
 * 재고 증감/설정/조회
 * */
@RestController
@RequestMapping(ApiMappingPattern.Stocks.ROOT)
@RequiredArgsConstructor
public class I_StockController {
    private final I_StockService stockService;

    @PostMapping(ApiMappingPattern.Stocks.ADJUST)
    public ResponseEntity<ResponseDto<StockResponse.Response>> adjust(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody StockRequest.StockAdjust req
            ) {
        ResponseDto<StockResponse.Response> response = stockService.adjust(userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ResponseDto<StockResponse.Response>> set(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody StockRequest.StockSet req
    ) {
        ResponseDto<StockResponse.Response> response = stockService.set(userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiMappingPattern.Stocks.PRODUCT_ID)
    public ResponseEntity<ResponseDto<StockResponse.Response>> getByProductId(
            @PathVariable Long productId
    ) {
        ResponseDto<StockResponse.Response> response = stockService.get(productId);
        return ResponseEntity.ok(response);
    }
}
