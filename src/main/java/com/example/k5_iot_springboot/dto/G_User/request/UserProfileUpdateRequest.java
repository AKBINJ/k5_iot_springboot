package com.example.k5_iot_springboot.dto.G_User.request;

import com.example.k5_iot_springboot.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @NotBlank @Size(max = 50)
        String nickname,
        Gender gender // 선택
) {

}
