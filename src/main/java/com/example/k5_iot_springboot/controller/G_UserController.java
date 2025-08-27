package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.dto.G_User.request.UserProfileUpdateRequest;
import com.example.k5_iot_springboot.dto.G_User.response.UserProfileResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.G_UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** 권한 전체 사용 가능 Controller - 권한과 상관없이 개인 정보 확인 & 수정 */
@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class G_UserController {
    private final G_UserService userService;

    // cf) hasRole("")                  - 권한 확인 (단일 권한)
    //     hasAnyRole("", "", "", ...)  - 권한 확인 (여러 권한)
    //     isAuthenticated()            - 인증 확인
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ResponseDto<UserProfileResponse.MyPageResponse>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ResponseDto<UserProfileResponse.MyPageResponse> response = userService.getMyInfo(principal);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ResponseDto<UserProfileResponse.MyPageResponse>> updateMyInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        ResponseDto<UserProfileResponse.MyPageResponse> response = userService.updateMyInfo(principal, request);
        return ResponseEntity.ok().body(response);
    }
}