package com.example.k5_iot_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration // 스프링 설정 클래스임을 명시
@EnableMethodSecurity(prePostEnabled = true)
// 스프링 시큐리에서 메서드 보안 기능을 활성화
// prePostEnabled = true 옵션
// : @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter 같은
//  , 어노테이션 기반 보안 기능 사용 가능
// >> 메서드 단위에서 접근 권한을 세밀하게 제어 가능
public class SecurityMethodConfig {
}
