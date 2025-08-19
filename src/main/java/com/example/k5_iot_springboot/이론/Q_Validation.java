package com.example.k5_iot_springboot.이론;

/*
* === Spring Boot Validation ===
*
* 1. Validation란?
* : 유효성 검증
* - 사용자가 입력한 값이 올바른 형식화 조건을 만족하는지 확인하는 과정
* - 사용) jakarta.validation 표준 어노테이션 사용
*
* 2. Validation 사용 목적
* - 잘못된 입력값(DTO)에 대한 DB 저장 방지 (데이터 무결성)
* - 사용자의 잘못된 입력을 빠르게 알려줌으로써 UX 향상 (User Experience: 사용자 경험)
*
* 3. Validation 사용 흐름
* 1) DTO(Data Transfer Object)에 어노테이션 작용
* 2) Controller 에서 @Valid 또는 @Validation과 함께 적용
* 3) 입력값이 조건을 만족하지 못하면 - 자동 예외 발생 (Method Argument Not Valid Exception)
*
* +) @RestControllerAdvice 또는 GlobalExceptionHandler로 처리 가능
*
* 4. 주요 Validation 어노테이션
*
* +) 각 어노테이션은 message 속성을 가짐
*       - 검증 실패 시 사용자에게 보여줄 오류 메시지를 직접 지정하는 역할
*
* 1) @NotNull
*   - 값이 null이 아니어야 함
*   - 단, 빈 문자열("")은 허용됨
*
* 2) @NotEmpty
*   - 값이 null이 아니고 빈 문자열도 아님
*   - 단, 공백(" ")은 허용됨
*
* 3) @NotBlank
*   - 값이 null도 아니고, 빈 문자열도 아니고, 공백만 있는 경우도 아님
*   - 문자열 필드 검증 시 가장 많이 사용
*
* cf) NonNull
*   : Lombok 어노테이션
*   - 반드시 초기화가 이루어져야하기 때문에 @RequiredArgsConstructor와 주로 사용
*
* 4) @Size
*   - 문자열, 배열, List, Set 등의 길이(크기) 제한
*   - @Size(min = 5, max = 10): 최소/최대값 설정 가능
*
* 5) @Min(0) / @Max(100)
*   - 숫자 범위 지정
*   - @Min(value = 0) / @Max(value = 100)
*
* 6) @Email
*   - 이메일 형식 검증
*
* 7) @Pattern
*   - 정규표현식으로 직접 패턴 검증 (사용자 정의 무결성)
*
* 8) @Positive, @PositiveOrZero, @Negative, @NegativeOrZero
*   - 양수, 음수만 허용 (포함 여부 선택 가능)
*
* 9) @Future, @Past
*   - 날짜 검증
*   - @Future: 예약일
*   - @Past : 생년월일
* */

public class Q_Validation {
}
