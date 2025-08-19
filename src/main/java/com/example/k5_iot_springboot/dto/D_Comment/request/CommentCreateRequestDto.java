package com.example.k5_iot_springboot.dto.D_Comment.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentCreateRequestDto(
        @NotBlank(message = "내용은 필수 입력 값입니다.")
        @Size(max = 1_000, message = "내용은 최대 1,000자까지 입력 가능합니다.")
        String content,

        @NotBlank(message = "작성자는 필수 입력 값입니다.")
        @Size(max = 100, message = "작성자는 최대 100자까지 입력 가능합니다.")
        String commenter
) {}