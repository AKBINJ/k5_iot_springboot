package com.example.k5_iot_springboot.handler;

import com.example.k5_iot_springboot.common.enums.ErrorCode;
import com.example.k5_iot_springboot.common.errors.ErrorResponse;
import com.example.k5_iot_springboot.common.errors.FieldErrorItem;
import com.example.k5_iot_springboot.dto.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
// 스프링이 빈으로 등록 - 해당 프로젝트 전역의 @RestController에서 발생하는 예외를 처리
// : 예외를 가로채어 JSON 표준 응답을 반환
/*
 * 1. 단일 책임 원칙 (SRP)
 *   - 예외 처리를 Controller가 아닌 GEH에서 담당
 *
 * 2. 재사용성 증가
 *   - 모든 Controller에 대한 예외 처리가 한 곳에서 관리
 *
 * 3. 가독성 향상
 * */
@Slf4j // lombok 어노테이션 - 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음
public class GlobalExceptionHandler {

    // == 공통 응답 생성 유틸 == //
    private ResponseEntity<ResponseDto<Object>> fail(
            ErrorCode code, String reason, List<FieldErrorItem> errors
            // : 실제 응답을 한 곳에서 조립
    ) {
        // 1) reason값 설정: 비워질 경우 ErrorCode의 기본 메시지 값 사용
        String finalReason = (reason != null && !reason.isBlank()) ? reason : code.defaultMessage;
        // 2) 클라이언트가 해석할 수 있는 표준 에러 응답 본문 조립
        ErrorResponse body = ErrorResponse.of(code.code, finalReason, errors);
        return ResponseEntity.status(code.status)
                .body(ResponseDto.setFailed(finalReason, body));
    }

    // == @Valid(@RequestBody) 검증 실패 항목을 표준 형식으로 변환 == //
    private List<FieldErrorItem> toFieldErrors(MethodArgumentNotValidException e) {
        List<FieldErrorItem> list = new ArrayList<>(); // 수집용 리스트

        // 필드 단위 검증 실패 항목 순회
        e.getBindingResult().getFieldErrors().forEach(err -> {
            list.add(new FieldErrorItem(
                    err.getField(),                                                 // 실패한 필드명
                    Objects.toString(err.getRejectedValue(), "null"),   // 거부된 값(널이면 null)
                    err.getDefaultMessage()                                         // 제약 메시지
            ));
        });

        // 글로벌 에러도 검증
        e.getBindingResult().getGlobalErrors().forEach(err -> {
            // 필드명 대신 오브젝트명 사용
            list.add(new FieldErrorItem(err.getObjectName(), "", err.getDefaultMessage()));
        });

        return list;
    }

    // === 400 Bad Request 그룹: 잘못된 인자/상태 (서비스 레벨 방어 예외 등) === //
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ResponseDto<Object>> handleBadRequest(Exception e) {
        log.warn("Bad Request: {}", e.getMessage());
        return fail(ErrorCode.BAD_REQUEST, null, null);
    }

    // === 400 Validation Error: @Valid @RequestBody 검증 실패 === //
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> handleValidation(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());
        return fail(ErrorCode.VALIDATION_ERROR, null, toFieldErrors(e));
    }

    // === 403 Forbidden: 접근 거부 === //
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto<Object>> handleAccessDenied(AccessDeniedException e) {
        log.warn("AccessDenied: {}", e.getMessage());
        return fail(ErrorCode.FORBIDDEN, null, null);
    }

    // === 404 Not Found: 엔티티 조회 실패 === //
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDto<Object>> handleNotFound(EntityNotFoundException e) {
        log.warn("Not Found: {}", e.getMessage());
        return fail(ErrorCode.NOT_FOUND, null, null);
    }

    // === 409 Conflict: 무결성 위반(중복/제약조건) === //
    @ExceptionHandler(DataIntegrityViolationException.class) // Unique 키 충돌, FK 위반 등
    public ResponseEntity<ResponseDto<Object>> handleConflict(DataIntegrityViolationException e) {
        log.warn("Conflict: {}", e.getMessage());
        return fail(ErrorCode.CONFLICT, null, null);
    }

    // === 500 Internal Server Error: 그 밖의 모든 예외에 대한 최종 안정망 === //
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleException(Exception e) {
        log.error("Internal error", e);
        return fail(ErrorCode.INTERNAL_ERROR, null, null);
    }
}