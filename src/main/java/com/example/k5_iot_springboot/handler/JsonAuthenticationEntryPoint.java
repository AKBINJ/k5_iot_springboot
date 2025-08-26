package com.example.k5_iot_springboot.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// AuthenticationEntryPoint 인터페이스
// : 인증되지 않은 사용자가 인증이 필요한 엔드포인트로 접근할 때 발생

// cf) 엔드포인트(endpoint)
// : 웹 애플리케이션에서 클라이언트가 서버에 요청을 보내는 특정 URL
// EX) /api/v1/boards, /api/v1/users

// 401 전용 Entry Point
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"result\":\"fail\",\"message\":\"인증이 필요합니다.\"}");
    }
}