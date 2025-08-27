package com.example.k5_iot_springboot.이론;

/*
    === 스프링 시큐리티 동작 흐름 ===

    1. 토큰 생성 (요청 자격 증명)
        # G_AuthServiceImpl
        - new UsernamePasswordAuthenticationToken(req.loginId(), req.password())
        - 상태: authenticated=false, principal=loginId, credentials=rawPassword

    2. AuthenticationManager.authenticate(...) 호출
        - 구현체는 ProviderManager
        - 내부에서 여러 AuthenticationProvider 리스트가 있고
            , 토큰 타입을 처리할 수 있는 Provider에게 순서대로 위임

    3. DaoAuthenticationProvider 담당
        - 해당 토큰의 타입(UsernamePasswordAuthenticationToken)을 지원하므로 처리 진입

    4. 사용자 로딩 (UserDetailsService)
        - CustomUserDetailsService.loadUserByUsername(loginId) 호출
        - UserRepository.findByLoginId(loginId)로 G_User 엔티티 조회
        - 조회 실패) UsernameNotFoundException 발생 ("아이디/비밀번호가 올바르지 않음"으로 외부 전달)

    5. 보안 표현으로 매핑
        - 조회된 G_User -> UserPrincipalMapper.map(user)
        - UserPrincipal 생성: id, username, password(해시, 암호화), authorities(ROLE_*), 계정상태 플래그

    6. 비밀번호 검증 (PasswordEncoder)
        - PasswordEncoder.matchers(rao, encoded)
            >> 불일치: BadCredentialsException
            >> 일치: 다음 단계 진행

    7. 계정 상태 점검
        - isAccountNonLocked, isEnabled, isAccountNonExpired, isCredentialsNonExpired
        - 하나라도 false면 각각의 예외 발생 (잠금, 비활성, 만료 등)

    8. 인증 성공 토큰 생성
        - 새로운 UsernamePasswordAuthenticationToken(principal, null, authorities)
        - 상태: authenticated=true, principal=UserPrincipal, credentials=null(보안상 삭제), authorities 포함

    9. AuthenticationManager가 성공 토큰을 반환
        - Authentication auth가 돌아옴 (G_AuthServiceImpl)
        - auth.getPrincipal(): UserPrincipal
        - auth.getAuthorities(): ROLE_*

    10. JWT 발급 (애프릴케이션 레벨)
        - auth 정보(사용자 PK, username, roles 등)로 Access Token 생성
        - 클레임 sub=username, roles, iat, exp

    11. 응답 작성
        - 헤더 Authorization: Bearer <토큰> 또는 바디 JSON 변환
        - 이후 클라이언트 요청마다 해당 토큰을 Authorization 헤더로 전송

    ------------
    +) 이후 요청 처리 (인가 단계)
        - JWT 필터가 헤더의 토큰 검증 >> 유효하면 UserPrincipal 재구성 >> SecurityContext에 주입
*/
public class V_SecurityContext {

}
