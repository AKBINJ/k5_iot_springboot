package com.example.k5_iot_springboot.dto.B_Student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter // 필수 - JSON 변환 시 필드 접근을 위해 사용
@Builder
@AllArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String name;
}
