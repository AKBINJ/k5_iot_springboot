package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.I_Order.request.ProductRequest;
import com.example.k5_iot_springboot.dto.I_Order.response.ProductResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.I_Product;
import com.example.k5_iot_springboot.entity.I_Stock;
import com.example.k5_iot_springboot.repository.I_ProductRepository;
import com.example.k5_iot_springboot.repository.I_StockRepository;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.I_ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class I_ProductServiceImpl implements I_ProductService {
    private final I_ProductRepository productRepository;
    private final I_StockRepository stockRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseDto<ProductResponse.DetailResponse> create(
            UserPrincipal userPrincipal,
            ProductRequest.@Valid Create req
    ) {
        ProductResponse.DetailResponse data = null;

        I_Product product = I_Product.builder()
                .name(req.name())
                .price(req.price())
                .build();

        I_Product saved = productRepository.save(product);

        stockRepository.save(
                I_Stock.builder()
                        .product(saved)
                        .build()
        );

        data = new ProductResponse.DetailResponse(saved.getId(), saved.getName(), saved.getPrice());

        return ResponseDto.setSuccess("제품이 성공적으로 등록되었습니다.", data);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseDto<ProductResponse.DetailResponse> update(
            Long productId,
            UserPrincipal userPrincipal,
            ProductRequest.@Valid Update req
    ) {
        ProductResponse.DetailResponse data = null;

        I_Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        if (req.name() == null && req.price() == null) {
            throw new IllegalArgumentException("제품을 수정할 데이터가 없습니다.");
        }

        boolean nameChanged = req.name() != null && !Objects.equals(product.getName(), req.name());
        boolean priceChanged = req.price() != null && !Objects.equals(product.getPrice(), req.price());

        if (!nameChanged && !priceChanged) {
            throw new IllegalArgumentException("변경된 데이터가 없습니다.");
        }

        // === null 값에 대한 연산, 값 꺼내오기의 오류!! ===
//        if (product.getName().equals(req.name()) && product.getPrice() == req.price()) {
//            throw new IllegalArgumentException("변경된 데이터가 없습니다.");
//        }

        if (nameChanged) product.setName(req.name());
        if (priceChanged) product.setPrice(req.price());

        data = new ProductResponse.DetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );

        return ResponseDto.setSuccess("제품이 성공적으로 수정되었습니다.", data);
    }

    @Override
    public ResponseDto<ProductResponse.DetailResponse> get(Long productId) {
        ProductResponse.DetailResponse data = null;

        I_Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        data = new ProductResponse.DetailResponse(product.getId(), product.getName(), product.getPrice());

        return ResponseDto.setSuccess("제품이 성공적으로 조회되었습니다.", data);
    }
}
