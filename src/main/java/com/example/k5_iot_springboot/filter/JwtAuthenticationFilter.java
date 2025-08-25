package com.example.k5_iot_springboot.filter;

import com.example.k5_iot_springboot.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * === JwtAuthenticationFilter ===
 * : JWT 인증 필터
 * - 요청에서 JWT 토큰을 추출
 * - request의 header에서 토큰을 추출하여 검증 (유효한 경우 Security의 context에 인증 정보 설정)
 *
 * cf) Spring Security가 OncePerRequestFilter를 상속받아 매 요청마다 실행
 * */
@Component // 스프링이 해당 클래스를 관리하도록 지정, 의존성 주입
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization"; // 요청 헤더 키
    private static final String BEARER_PREFIX = JwtProvider.BEARER_PREFIX;

    private final JwtProvider jwtProvider; // 의존성 주입

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 이미 인증된 컨텍스트가 있으면 스킵 (다른 필터가 인증처리를 한 경우)
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Authorization 헤더에서 JWT 토큰 추출
            String authorization = request.getHeader(AUTH_HEADER);

            // 헤더에서 토큰을 파싱하여 가져옴 ("Bearer " 제거)
            String token = (authorization != null && authorization.startsWith(BEARER_PREFIX))
                    ? jwtProvider.removeBearer(authorization)
                    : null;

            // 토큰이 없거나 유효하지 않으면 필터 체인을 타고 다음 단계로 이동
            if (token == null || token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 유효성 검사(서명/만료 포함)
            if (!jwtProvider.isValidToken(token)) {
                filterChain.doFilter(request, response); // 토큰이 유효하지 않은 경우 - 시큐리티 설정 없이 로직 실행
                return;
            }

            String username = jwtProvider.getUsernameFromJwt(token);
            Set<String> roles = jwtProvider.getRolesFromJwt(token);

            // 권한 문자열 - GrantedAuthority로 매핑 ("ROLE_" 접두어 보장)
            Collection<? extends GrantedAuthority> authorities = toAuthorities(roles);

            // SecurityContext 설정
            setAuthenticationContext(request, username, authorities);

        } catch (Exception e) {
            logger.warn("JWT filter error", e);
        }
        // 체인 계속
        filterChain.doFilter(request, response);
    }

    /** SecurityContextHolder에 인증 객체 세팅 */
    private void setAuthenticationContext(
            HttpServletRequest request,
            String username,
            Collection<? extends GrantedAuthority> authorities
    ) {
        // 사용자 아이디 (또는 고유 데이터)를 바탕으로 인증 토큰 생성
        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // 요청에 대한 세부 정보 설정
        // : 생성된 인증 토큰에 요청의 세부사항 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 빈 SecurityContext 객체 생성 - 인증 토큰 주입
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        // SecurityContextHolder에 생성된 컨텍스트 설정
        SecurityContextHolder.setContext(context);
    }

    /** USER/ADMIN -> "ROLE_USER"/"ROLE_ADMIN" 으로 매핑 */
    private List<GrantedAuthority> toAuthorities(Set<String> roles) {
        if (roles == null || roles.isEmpty()) return List.of(); // 권한이 없으면 빈 배열 반환
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}