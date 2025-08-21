package com.example.k5_iot_springboot.dto.F_Board.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 게시글 요청 DTO
 * - Controller 바인딩용
 * */
public class BoardRequestDto {
    /**
     * 게시글 생성 요청
     * */
    public record CreateRequest(
            @NotBlank(message = "제목은 비어있을 수 없음")
            @Size(max = 100, message = "제목은 최대 100자")
            String title,

            @NotBlank(message = "내용은 비어 있을 수 없음")
            String content
    ) {}

    /**
     * 게시글 생성 요청
     * */
    public record UpdateRequest(
            @NotBlank(message = "제목은 비어있을 수 없음")
            @Size(max = 100, message = "제목은 최대 100자")
            String title,

            @NotBlank(message = "내용은 비어 있을 수 없음")
            String content
    ) {}
}
