package com.example.k5_iot_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 로그인 아이디*/
    @Column(nullable = false, unique = true)
    private String username;

    /* refresh token 문자열 */
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    /* 만료 시각 (밀리초 단위) */
    @Column(nullable = false)
    private Long expiry;

}
