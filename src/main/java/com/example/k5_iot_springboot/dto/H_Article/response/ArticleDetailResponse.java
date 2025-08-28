package com.example.k5_iot_springboot.dto.H_Article.response;

import com.example.k5_iot_springboot.entity.H_Article;

import java.time.LocalDateTime;

public record ArticleDetailResponse (
        Long id,
        String title,
        String content,
        String authorLoginId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ArticleDetailResponse from(H_Article article) {
        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getLoginId(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

}
