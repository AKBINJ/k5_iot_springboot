package com.example.k5_iot_springboot.dto.G_User.request;

import com.example.k5_iot_springboot.common.enums.RoleType;
import jakarta.validation.constraints.NotNull;

public record RoleModifyRequest(
        @NotNull
        RoleType role // 추가, 삭제 대상으로 사용할 역할
) {
}
