package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.G_UserRole;
import com.example.k5_iot_springboot.entity.G_UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface G_UserRoleRepository extends JpaRepository<G_UserRole, G_UserRoleId> {
    List<G_UserRole> findByIdUserId(Long userId);
}
