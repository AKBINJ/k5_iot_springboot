package com.example.k5_iot_springboot.이론;

/*
    === 요청(Request) & 응답(Response) ===

    1. 요청
        1) start line: Request Message의 시작 라인 (3가지로 구성)
            - HTTP 메서드: GET, POST, PUT, DELETE 등
            - Request Target: HTTP Request가 전송되는 목표 주소
            - HTTP Version: version에 따라 Request 메시지 구조나 데이터가 상이할 수 있음 (명시)

        2) headers: 해당 Request에 대한 추가 정보를 담는 부분
        3) body: 해당 Request가 전송하는 데이터를 담는 부분 (전송하는 데이터가 없으면 비워짐)

    2. 응답
        1) status line: Response의 상태를 간략하게 나타냄
            -HTTP Version
            - Status code
            - Status Text
        2) headers
        3) body: 데이터를 전송할 필요가 없는 경우 생략 가능


    === 스프링부트 응답 데이터 정형화 ===
    : 데이터 전송의 일관성르 유지하기 위해 사용
    - 클라이언트(프론트엔드)가 항상 예측 가능한 구조의 응답을 받을 수 있도록 설계

    1) ResponseEntity
        : 스프링에서 HTTP 응답 전체를 제어할 수 있게 해주는 클래스
        - 상태 코드, 헤더, 바디 포함

    2) ResponseDto
        : 응답 바디의 형식을 정형화해서 데이터를 감싸는 DTO 클래스 - 사용자 정의
        - 일관된 구조를 제공
*/

public class M_Request_Response {
}
