package com.example.k5_iot_springboot.dto.G_Admin.request;

import com.example.k5_iot_springboot.common.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public class RoleManageRequest {
    /** 특정 사용자(userId)의 권한을 해당 Set으로 교체(갱신) */
    public record UpdateRolesRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long userId,

            @NotEmpty(message = "roles는 비어있을 수 없습니다.")
            Set<@NotNull(message = "roles 항목은 null일 수 없습니다.") RoleType> roles
    ) {}

    /** 특정 사용자(userId)에 단일 권한을 추가 */
    public record AddRoleRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long userId,
            @NotNull(message = "role은 필수입니다.")
            RoleType role
    ) {}

    /** 특정 사용자(userId)에서 단일 권한을 제거 */
    public record RemoveRoleRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long userId,
            @NotNull(message = "role은 필수입니다.")
            RoleType role
    ) {}
}