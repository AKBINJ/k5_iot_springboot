package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findAllByUsername(String username);
    void deleteByUsername(String username);
}
