package com.example.k5_iot_springboot.이론;

/*
    cf) 인터넷의 기본 개념
        1) IP(Internet Protocol) 주소: 각 장치의 고유 주소
        2) DNS: 도메인 이름을 IP 주소로 변환 (google.com >>> 172.217.@@@.XXX)
        3) PORT 번호: 하나의 IP에서 다양한 프로그램 서버를 구분

        4) TCP/IP: 신뢰성 있는 데이터 전송을 위한 통신 규약
            - HTTP(웹 브라우징 전송)
            - FTP(파일 전송)
            - SMTP(이메일 전송)

        cf) HTTP 기본 포트: 80
            HTTPS 기본 포트: 443 (Security)
            +) Tomcat은 주로 8080 포트를 사용

    1. HTTP 메서드 (Method)
    : 요청이 어떤 목적을 가지고 있는지 명시하는 방법

        1) GET(가져오기) - 서버로 부터 데이터를 가져올 것을 요청
        2) POST(생성하기) - 서버에 새로운 데이터를 저장
        3) PUT(수정하기) - 서버의 기존 데이터 전체를 수정
            cf) UPDATE
                >> 서버의 기존 데이터의 일부를 수정
        4) DELETE(삭제하기) - 서버의 데이터를 삭제

        EX) BoardController (게시판에 대한 요청을 "처리"하는 Controller 클래스)
            - CRUD
            [POST] / boards : 게시글을 저장해줘
            [GET] / boards : 게시글 전체를 조회해줘
            [GET] / boards / 고유ID : 해당 ID의 게시글을 조회해줘
            [PUT] / boards / 고유ID : 해당 ID의 게시글을 수정해줘
            [DELETE] / boards / 고유ID : 해당 ID의 게시글을 삭제해줘

    2. HTTP 상태 코드 (Status Code)
    : 요청에 대한 처리 결과를 숫자와 간단한 메시지로 알려줌

        1) 200 번대
            200 OK: 성공 (요청한 작업 성공 - GET/PUT/DELETE)
            201 Created: 생성 성공 (새로운 데이터 생성 성공 - POST)

        2) 400 - 개발자가 처리해야 하는 문제
            400 Bad Request: 잘못된 요청 (요청 데이터 누락 및 유효성 오류 & 요청 메서드 오류)
            401 Unauthorized: 인증 실패
                (EX. 사원증 없이 회사 출입 금지, 카페 가입 없이 내용 확인 불가)
            403 Forbidden: 권한 없음
                (EX. 회사 내 보안 문서는 권한 없이 열람 불가, 카페 권한 없이는 글쓰기 불가
            404 NOT FOUND: 요청 자체를 찾을 수 없음 (해당하는 요청이 아예 없음)

        3) 500 - 개발자가 해결할 수 없는 문제
            500 Internal Serval Error: 서버 문제


*/
public class B_HTTP02 {
}
