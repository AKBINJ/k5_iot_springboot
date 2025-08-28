package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.dto.H_Article.request.ArticleCreateRequest;
import com.example.k5_iot_springboot.dto.H_Article.request.ArticleUpdateRequest;
import com.example.k5_iot_springboot.dto.H_Article.response.ArticleDetailResponse;
import com.example.k5_iot_springboot.dto.H_Article.response.ArticleListResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.H_ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class H_ArticleController {
    private final H_ArticleService articleService;

    /** 게시글 생성: 인증된 사용자만 생성 가능 */
    @PostMapping
    public ResponseEntity<ResponseDto<ArticleDetailResponse>> createArticle(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ArticleCreateRequest request
    ) {
        ResponseDto<ArticleDetailResponse> response = articleService.createArticle(principal, request);
        return ResponseEntity.ok().body(response);
    }

    // 조회 (전체 조회)
    @GetMapping
    public ResponseEntity<ResponseDto<List<ArticleListResponse>>> getAllArticles() {
        ResponseDto<List<ArticleListResponse>> response = articleService.getAllArticles();
        return ResponseEntity.ok().body(response);
    }

    // 조회 (단건 조회)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ArticleDetailResponse>> getArticleById(
            @PathVariable Long id
    ) {
        ResponseDto<ArticleDetailResponse> response = articleService.getArticleById(id);
        return ResponseEntity.ok().body(response);
    }

    // 수정: 작성자 | MANAGER | ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ArticleDetailResponse>> updateArticle(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ArticleUpdateRequest request
    ) {
        ResponseDto<ArticleDetailResponse> response = articleService.updateArticle(principal, id, request);
        return ResponseEntity.ok().body(response);
    }

    // 삭제: 작성자 | ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteArticle(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        ResponseDto<Void> response = articleService.deleteArticle(principal, id);
        return ResponseEntity.ok().body(response);
    }
}
