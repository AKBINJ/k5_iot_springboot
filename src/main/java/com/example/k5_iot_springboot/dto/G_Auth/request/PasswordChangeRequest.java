package com.example.k5_iot_springboot.dto.G_Auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest (
        @NotBlank @Size(min = 8, max = 100)
        String newPassword
) {
}
