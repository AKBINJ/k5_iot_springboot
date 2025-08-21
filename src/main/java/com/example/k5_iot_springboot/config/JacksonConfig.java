package com.example.k5_iot_springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson에 JavaTimeModule 등록 + 타임 스탬프(숫자) 대신 ISO-8601 문자열로 출력
 * : LocalDateTime 드으이 직렬화/역직렬화가 자연스럽게 동작
 * */
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() { // ObjectMapper 클래스 : 직렬화/역직렬화 담당
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // java 8 날짜/시간 지원

        // 숫자 타임스탬프 출력 비활성화: "yyyy-MM-dd'T'HH:mm:ss"형태의 문자열로 (ISO-8601)
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return om;
    }
}
