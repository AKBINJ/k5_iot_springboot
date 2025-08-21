package com.example.k5_iot_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing을 전역 설정
 * - @CreatedDate, @LastModifiedDate 등이 동작하려면 필수
 * */
@Configuration // 전역 설정
@EnableJpaAuditing // JpaAuditing 사용 설정
public class JpaAuditingConfig {
}
