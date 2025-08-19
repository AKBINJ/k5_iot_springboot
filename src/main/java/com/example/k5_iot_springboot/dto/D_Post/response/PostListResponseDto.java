package com.example.k5_iot_springboot.dto.D_Post.response;

import com.example.k5_iot_springboot.entity.D_Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostListResponseDto(
        Long id,
        String title,
        String content,
        String author
) {
    public static PostListResponseDto from(D_Post post) {
        if (post == null) return null;
        return new PostListResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor()
        );
    }

    public PostListResponseDto summarize(int maxLen) {
        String summarized = content == null ? null :
                (content.length() <= maxLen ? content : content.substring(0, maxLen) + "...");
        return new PostListResponseDto(id, title,  summarized, author);
    }
}