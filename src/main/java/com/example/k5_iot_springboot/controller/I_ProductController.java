
package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.common.constants.ApiMappingPattern;
import com.example.k5_iot_springboot.dto.I_Order.request.ProductRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.ProductResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.I_ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 제품 등록/수정/조회
 * : 권한 'USER', 'MANAGER', 'ADMIN' 중 (등록, 수정)은 ADMIN만 (조회)는 누구나 가능
 * */
@RestController
@RequestMapping(ApiMappingPattern.Products.ROOT)
@RequiredArgsConstructor
public class I_ProductController {

    private final I_ProductService productService;

    // 제품 등록
    @PostMapping
    public ResponseEntity<ResponseDto<ProductResponse.DetailResponse>> create(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProductRequest.Create req
    ) {
        ResponseDto<ProductResponse.DetailResponse> response
                = productService.create(userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    // 제품 수정
    @PutMapping(ApiMappingPattern.Products.ID_ONLY)
    public ResponseEntity<ResponseDto<ProductResponse.DetailResponse>> update(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProductRequest.Update req
    ) {
        ResponseDto<ProductResponse.DetailResponse> response
                = productService.update(productId, userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    // 제품 조회
    @GetMapping(ApiMappingPattern.Products.ID_ONLY)
    public ResponseEntity<ResponseDto<ProductResponse.DetailResponse>> getProductById(
            @PathVariable Long productId
    ) {
        ResponseDto<ProductResponse.DetailResponse> response
                = productService.get(productId);
        return ResponseEntity.ok(response);
    }
}
