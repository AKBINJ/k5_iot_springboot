package com.example.k5_iot_springboot.common.errors;

import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponse(
        String code, // 에러 코드
        String reason, // 사용자용 요약 메시지
        List<FieldErrorItem> errors // 검증 실패 시 필드 오류 목록
){
    public static ErrorResponse of(String code, String reason){
        return new ErrorResponse(code, reason, List.of());
    }

    public static ErrorResponse of(String code, String reason, List<FieldErrorItem> errors) {
        return  new ErrorResponse(code, reason, errors != null ? errors : List.of());
    }
}
