package com.example.k5_iot_springboot.이론;

/*
* === RESTful API ===
*
* 1. REST란?
*   REpresentaion State Transfer
*   : 자원(Resource)을 URI로 식별하고
*   , HTTP 메서드를 이용해 해당 자원에 대한 행위를 정의하는 아키텍처 스타일
*
*   - 자원에 표현 : JSON, XML 등
*   - 상태 전달: 요청(Request)과 응답(Response)으로 상태 전송
*   - HTTP 표준 메서드를 활용
*
*   >> 네트워크 상에서 클라이언트와 서버 간의 통신 방식 중 하나
*
* 2. RESTful API란?
*   : REST의 제약 조건을 준수하는 API
*   - URI에 명사 사용 (행위 HTTP 메서드로 표현)
*   - 계층 구조를 URI로 표현
*   - 대소문자, 언더바(_) 사용 규칙 준수
*   - HTTP 상태코드로 결괄르 명확하게 전달
*
* 3. RESTful API 베스트 프렉티스
*
* 1) 명사 사용 - 리소스명을 동사가 인니 명사로 작성
*  (O) / users, /products, /carts, /meetings 등
*  (X) /getUsers, /matchMembers
*
* 2) 소문자 사용 - 대문자 금지
*   (O) /menus, /colors
*   (X) /Menus, /Products
*
* 3) 언더바 X, 하이픈 O - 단어의 구분은 하이픈으로 표현
*   (O) /user-profiles, /school-numbers
*
* 4) 마지막 / 금지 - URI 끝에 / 를 남기지 않음
*   (O) /users/1, /products/search/name
*
* 5) 계층 구조 표현 - 관계는 / 로 표현
*   (O) /users/{userId}/orders/{orderId} - 특정 사용자(userId)의 주문들 중 특정 주문(orderId)
*
* 6) 복수형 명사 사용 - 특정 데이터는 복수형 명사 뒤 계층 구조로 표시
*   (O) /users, /posts, /books, /menus
*
* +) 버저닝 사용 - API의 시작은 'api/v1' 형태 사용
*
* === RESTful API 예시 ===
* 케이스1) 사용자/인증
*   - 회원가입: POST /api/v1/auth/signup
*   - 로그인: POST / api/v1/auth/login
*   - 내 정보 조회: GET/ api/v1/users/me (로그인 한 자신의 정보 조회)
*   - 이메일 중복 체크: GET /api/v1/auth/check-email?email=XXX...)
*
* 케이스2) 쇼핑몰
*   - 상품 목록: GET /api/v1/products
*   - 상품 리뷰 조회: GET /api/v1/products/{productId}/reviews
*   - 장바구니: GET / api/v1/users/me/cart (사용자 1인 당 장바구니는 1개)
*   - 주문 생성: POST /api/v1/orders
*
* 케이스3) 게시판
*   - 게시글 목록: GET /api/v1/posts?category=notice&search=java&page=1&size=10
*   - 게시글 수정: PUT /api/v1/posts/{postId}
*   - 댓글 추가: POST /api/v1/posts/{postId}/comments
* */

public class P_RESTful {
}
