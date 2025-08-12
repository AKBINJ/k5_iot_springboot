package com.example.k5_iot_springboot.이론;

/*
    ResponseEntity<T>
    : HTTP 응답을 상태코드(Status Code), 헤더(Headers), 바디(Body)로 완전히 제어하는 컨테이너

    1) 타입
        : 제네릭 T 타입
        - 응답 바디의 타입 (UserResponseDto, List<UserResponseDto>, Void 등)
    2) 상속
        : HttpEntity<T>를 상속하고 status 필드를 추가한 형태

    3) 특징
        - 상태 코드/헤더 직접 설정 용이
        - DTO로 감싼 일관된 응답 포맷 사용 시 (ResponseEntity<ResponseDto<T>>)

    // 200 OK
    return ResponseEntity.ok(body);

    // 201 Created (+Location 헤더)
    return ResponseEntity.created(uri).body(응답데이터);

    // 204 No Content (바디 없음
    return ResponseEntity.noContent().build();

    // 404 NotFound
    return ResponseEntity.notFound().build();

    // 400 Bad Request (+바디)
    return ResponseEntity.badRequest().body(에러객체);
*/

public class N_ResponseEntity {
}
