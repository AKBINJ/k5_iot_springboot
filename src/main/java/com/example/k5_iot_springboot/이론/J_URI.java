
package com.example.k5_iot_springboot.이론;

/*
    === URI VS URL ===
    1. URI (Uniform Resource Identifier)
    : 웹의 자원을 식별하는 이름표 (문자열)
    - 웹 페이지, 이미지, 파일, 서비스 "엔드 포인트"

    2. URL (Uniform Resource Locator)
    : 그 자원이 어떻게/어디로 가서 접근하는지 알려주는 주소 + 방법 체계
    > 자원의 위치를 나타내는 문자열, 웹 주소를 의미

    cf) URL은 URI의 한 종류 (URI가 더 포괄적 개념)

    https://n.news.naver.com/mnews/article/018/0006087057?sort=asc
    >> URL
        - https: 스킴(프로토콜, 접근 방법)
        - n.news.naver.com: 호스트+포트(localhost:8080 - 어느 컴퓨터인지, 어떤 서버인지)
        - mnews/article: 경로(path, 자원을 나타냄)
        - sort=asc: 쿼리(추가 조건)

    === @RequestMapping은 URI 자원을 명시 ===
    : 해당 요청으로 어떠한 자원에 접근할 것인지 작성


    === HTTP 메서드와 @RequestMapping ===
    @RequestMapping("/test") - 클래스와의 연결
    : http://localhost:8080/test

    @PostMapping

    @GetMapping("/all")
    : http://localhost:8080/test/all

    @GetMapping("/{id}")
    : http://localhost:8080/test/1
    - 경로의 id 값을 사용하여 데이터를 구분

    @PutMapping

    @DeleteMapping("/{id}")
*/

public class J_URI {
}
