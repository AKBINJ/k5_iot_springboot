package com.example.k5_iot_springboot.이론;

/*
    === 스프링 시큐리티(Spring Security, 보안) ===
    : Spring Framework 기반 애플리케이션에서 보안(인증, 인가, 권한) 담당하는 보안 프레임워크
    - 다양한 어노테이션으로 CSRF 공격, 세션 고정 공격을 방어
    - 요청 헤더에 포함된 보안 처리도 가능

    1. 인증(Authentication)
        EX) 신분 확인
        : 사용자가 누구인지 확인하는 과정, 신원 입증 과정
        - 사용자가 로그인 기능을 통해 아이디, 비밀번호를 입력하면
            , 해당 데이터들을 기반으로 사용자가 주장하는 인물인지 확인

    2. 인가(Authorization)
        EX) 권한 확인
        : "인증된" 사용자가 특정 리소스(데이터)에 접근할 수 있는 권한인지 확인하는 과정
        - 관리자만 특정 페이지에 접근하거나 특정 테이터를 호출활 수 있도록 설정

    cf) CSRF (Cross-Site- Request Forgery)
        : 자신(인증된 사용자)의 의지와는 상관없이 특정 웹 애플리케이션에
            , 공격자가 원하는 요청을 보내도록 유도하는 웹보안 취약점

    === Security 사용 목적 ===
    1. 보한 기본기 자동 제공: 비밀번호 암호화, 세션/토큰 인증, 권한 체크, CRUD 보호
    2. 표준화된 구조: 보안 코드가 일관되게 유지 보수 쉬움
    3. 유연한 확장성: 소셜 로그인, OAuth2, JWT, 2FA 등으로 확장 가능.
    4. 필수 방어막: 인증/인가 없는 서비스는 보안 취약

    === Security 선행 개념 ===
    1. HTTP는 무상태(Stateless)
        : 인터넷의 HTTP 통신은 기억력이 없음
        - 서버는 이전 요청에 대한 사용자 정보를 기억하지 않음 (매번 로그인 정보를 다시 제출해야 함)
    2. 세션(Session) VS 토큰(JWT)
        1) 세션: 서버에 "누가 로그인했는지" 기록, 클라이언트는 세션 ID 쿠키만 전달
            - 서버가 로그인 상태 기억
        2) JWT: 서버는 토큰만 검증하고 별도의 저장소가 없어도 됨(완전한 Stateless)
            - 서명된 토큰으로 인증
    3. 암호화 / 해싱
        - 비밀번호 암호화 (문자열 그대로 저장하면 보안성 저하)
            >> 해싱(Hashing): 긴 안호화문으로 변환
            >> 스프링 시큐리티는 주로 BCrypt 사용 (매번 다른 해시 결과로 만들어짐 - 안전성 향상)

    4. Principal (주요한)
        - "인증된" 사용자 정보 (아이디, 이메일 등)
    5. Authority(권한), ROLE(역할)
        - 역할에 따라 권한이 달라짐
        >> 해당 요청(해당 URL)은 권한 ROLE_ADMIN 만 들어갈 수 있음의 규칙을 정의하는 것
    6. SecurityContext
        : 현재 요청의 인증/권한 저장
        - 현재의 사용자 기록
    7. Filter Chain
        - 요청을 차례로 검사하는 필터 묶음
        EX) '교문' > '복도' > '문앞' 검사
    8. UserDetailsService
        - 사용자 조회 서비스 (DB에서 사용자 찾기)
    9. PasswordEncoder
        - 비밀번호 해시/검증기: BCrypt로 저장/비교
    10. AuthenticationManager/Provider
        - 인증 총괄/실행
    11. CSRF/CORS
        - 웹 보안 규칙(폼 위조/다른 도메인 호출 규칙)

    === Security 요청 처리 흐름 ===
    [브라우저] -> [Security Filter Chain] -> [인증 필요] -> [인증 시도]

    +) 인증 성공: SecurityContext에 저장 -> [Controller(컨트롤러)]
       인증 실패: 401(Unauthorized, 로그인 X) / 403(Forbidden, 권한 X)

    >> 모든 요처은 필터 페인을 통과
    >> 인증이 필요한 URL이면 로그인 정보 확인 + 성공 시 SecurityContext에 사용자/권한을 저장

    === Spring Security 전체 처리 절차 ===
    1) HTTP 요청
        : 사용자가 브라우저/앱에서 요청(예: /login)을 보냄
            +) 데이터나 인증 정보(JWT 토큰)가 포함되어 있을 수도 있고, 없을 수도 있음
    2) SecurityFilterChain - 검문소 라인
        : 모든 요청(인증 정보 여부 상관 X)
        - 로그인 요청인지(UsernamePasswordAuthenticationFilter)
        - JWT 토큰이 있는지(JwtAuthenticationFilter, 커스텀 Filter에 전달)
        - 보호된 URL인지 확인

    3) AuthenticationManager/Provider - 신분 확인 부서
        : 로그인 시도(/login)가 들어오면, 필터가 아이디/비번을 요청에서 꺼냄
            >> AuthenticationManager에 전달
                (UsernamePasswordAuthenticationToken - 인증용 객체)
            >> 여러 AuthenticationProvider에게 일을 시킴
                (Manger가 provider 에게)
                - 해당 아이디/비밀번호 일치 확인
                - DB 기반 로그인, 소셜 로그인, JWT 검증 등 다양한 Provider가 존재

    4) UserDetailsService - 사람 찾기 창구
        : Provider가 아이디를 받으면 UserDetailsService를 호출
            >> DB에서 해당 유저 정보를 찾음
            >> 찾은 결과를 UserDetails 객체로 변환

    5) PasswordEncoder - 비밀번호 검증 담당
        : UserDetailsService에서 가져온 비밀번호는 암호화(BCrypt 해시) 되어 있음
        - 사용자가 입력한 비밀번호와 DB 해시값을 Encoder가 비교
            >> 일치하면 인증 성공, 틀리면 실패

    6) SecurityContext/SecurityContextHolder - 현재 로그인 정보 저장소
        : 인증 성공 시 스프링 시큐리티는 SecurityContext 안에 로그인된 유저 정보를 저장
            >> SecurityContextHolder에 보관, 같은 요청 시 언제든지 꺼낼 수 있음
            >> 서비스/컨트롤러에서 Authentication 타입의 매개변수로 로그인 사용자 정보 접근 가능

    7) Authorities (권한) - 어디까지 접근할 수 있는지 확인
        : UserDetails 안에 유저가 가진 권한(ROLE) 정보 저장
        - 필터체인은 요청된 URL과 권한을 비교
            >> 맞으면 통과, 아니면 막힘

    8) EntryPoint / AccessDeniedHandler - 실패 처리
        : 만약 인증이 안 됐거나(로그인 X)
            , 권한이 부족하면
        - AuthenticationEntryPoint (로그인 필요): 401 Unauthorized
        - AccessDeniedHandler (권한 없음) : 403 Forbidden
*/

public class T_Security {
}
