package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.G_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface G_UserRepository extends JpaRepository<G_User, Long> {
    Optional<G_User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}