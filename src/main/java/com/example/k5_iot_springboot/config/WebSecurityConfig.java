
package com.example.k5_iot_springboot.config;

import com.example.k5_iot_springboot.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/*
 * === WebSecurityConfig ===
 * : Spring Security를 통해 웹 애플리케이션의 보안을 구성 (보안 환경설정)
 * - JWT 필터를 적용하여 인증 처리, CORS 및 CSRF 설정을 비활성화
 *   >> 서버 간의 통신을 원활하게 처리
 * */
@Configuration // 해당 클래스가 Spring의 설정 클래스로 사용됨을 명시
@EnableWebSecurity // Spring Security의 웹 보안 활성화
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // properties 주입 데이터
    @Value("${cors.allowed-origins:*}") // https://app.example.com, https://admin.example.com
    private String allowedOrigins;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.exposed-headers:Authorization,Set-cookie}")
    private String exposedHeaders; // 필요한 헤더만 노출

    @Value("${security.h2-console:true}") // 로컬 개발 시 true
    private boolean h2ConsoleEnabled;

    /* ==========
     * PasswordEncoder / AuthManager
     * ========== */

    /** 비밀번호 인코더: 실무 기본 BCrypt (강도 기본값) */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Spring이 구성한 것을 노출 */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /* ==========
     * CORS
     *
     * cf) CORS (Cross Origin Resource Sharing)
     *  : 브라우저(예: 4178)에서 다른 도메인(Tomcat 서버: 8080)으로부터 리소스를 요청할 때 발생하는 보안 정책
     *  - REST API 사용 시 다른 출처(도메인)에서 API에 접근할 수 있도록 허용하는 정책
     *
     * corsConfigurationSource 메서드
     * : 특정 출처에서 온 HTTP 요청을 허용하거나 거부할 수 있는 필터
     * ========== */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = splitToList(allowedOrigins);

        config.setAllowCredentials(true);                           // 쿠키/자격 증명 헤더 허용
        config.setAllowedHeaders(splitToList(allowedHeaders));      // 요청 헤더 화이트리스트
        config.setAllowedMethods(splitToList(allowedMethods));      // 허용 메서드
        config.setExposedHeaders(splitToList(exposedHeaders));      // 응답에서 클라이언트가 읽을 수 있는 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 동일 CORS 정책 적용
        return source;
    }

    /* ==========
     * Security Filter Chain
     * : 보안 필터 체인 정의, 특정 HTTP 요청에 대한 웹 기반 보안 구성
     * - CSRF 보호를 비활성화, CORS 정책을 활성화
     * ==========*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm
                        -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 예외 처리 지점 (필요 시 커스텀 핸들러 연결)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/v1/auth/**"
                        ).permitAll()
                        // 읽기 공개 예시 (게시글 목록, 조회 등)
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/**").permitAll()
                        .anyRequest().authenticated() // 나머지는 인증 필요
                );
//        if (h2ConsoleEnabled) {
//            http.headers(headers
//                    -> headers.frameOptions(frame -> frame.sameOrigin()));
//            http.authorizeHttpRequests(auth
//                    -> auth.requestMatchers("/h2-console/**").permitAll());
//        }

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 배치
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static List<String> splitToList(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
