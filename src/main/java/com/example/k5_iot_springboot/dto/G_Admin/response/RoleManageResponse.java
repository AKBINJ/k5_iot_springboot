package com.example.k5_iot_springboot.dto.G_Admin.response;

import com.example.k5_iot_springboot.common.enums.RoleType;

import java.time.LocalDateTime;
import java.util.Set;

public final class RoleManageResponse {
    public record UpdateRolesResponse(
            Long userId,
            String loginId,
            Set<RoleType> roles,
            LocalDateTime updatedAt
    ) {}

    public record AddRoleResponse(
            Long userId,
            String loginId,
            RoleType added,         // 추가된 권한
            Set<RoleType> roles,    // 변경 후 최종 권한 목록
            LocalDateTime updatedAt
    ) {}

    public record RemoveRoleResponse(
            Long userId,
            String loginId,
            RoleType removed,        // 제거된 권한
            Set<RoleType> roles,     // 변경 후 최종 권한 목록
            LocalDateTime updatedAt
    ) {}
}