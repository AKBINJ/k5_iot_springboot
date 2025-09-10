package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.common.enums.RoleType;
import com.example.k5_iot_springboot.dto.G_Admin.request.RoleManageRequest;
import com.example.k5_iot_springboot.dto.G_Admin.response.RoleManageResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.G_Role;
import com.example.k5_iot_springboot.entity.G_User;
import com.example.k5_iot_springboot.repository.G_RoleRepository;
import com.example.k5_iot_springboot.repository.G_UserRepository;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.G_AdminService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class G_AdminServiceImpl implements G_AdminService {
    private final G_UserRepository userRepository;
    private final G_RoleRepository roleRepository;

    @Override
    @Transactional
    public ResponseDto<RoleManageResponse.UpdateRolesResponse> replaceRoles(
            UserPrincipal principal, RoleManageRequest.@Valid UpdateRolesRequest req
    ) {
        // 1) 갱신될 사용자 정보 조회
        G_User user = userRepository.findWithRolesById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 user가 없습니다."));

        // 2) 전체 교체 - 요청에 @NotEmpty이므로 최소 1개 이상의 권한을 보장!
        user.getUserRoles().clear();
        // req.roles().forEach(user::addRole);

        userRepository.flush();

        RoleManageResponse.UpdateRolesResponse data = new RoleManageResponse.UpdateRolesResponse(
                user.getId(),
                user.getLoginId(),
                Set.copyOf(user.getRoleTypes()), // 방어적 복사 - JPA 엔티티 컬렉션 필드를 DTO 내부에서 조작할 경우 캡슐화 깨짐 위험
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @Transactional
    public ResponseDto<RoleManageResponse.AddRoleResponse> addRole(UserPrincipal principal, RoleManageRequest.@Valid AddRoleRequest req) {
        G_User user = userRepository.findWithRolesById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 사용자가 없습니다."));

        G_Role role = roleRepository.findById(req.role())
                        .orElseThrow(()-> new EntityNotFoundException("해당 권한을 찾을 수 없습니다."));

        user.grantRole(role);

        userRepository.flush();

        RoleManageResponse.AddRoleResponse data = new RoleManageResponse.AddRoleResponse(
                user.getId(),
                user.getLoginId(),
                req.role(),
                Set.copyOf(user.getRoleTypes()),
                user.getUpdatedAt()
        );
        return ResponseDto.setSuccess("SUCCESS", data);
    }

    // 권한 제거 (권한 회수 - 최소 1개 권한 보장
    @Override
    @Transactional
    public ResponseDto<RoleManageResponse.RemoveRoleResponse> removeRole(UserPrincipal principal, RoleManageRequest.@Valid RemoveRoleRequest req) {
        G_User user = userRepository.findWithRolesById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 사용자가 없습니다."));

        G_Role role = roleRepository.findById(req.role())
                .orElseThrow(() -> new EntityNotFoundException("해당 권한을 찾을 수 없습니다."));

        user.revokeRole(role); // orphanRemoval=true -> 자동 제거

        userRepository.flush();

        // 비워지는 경우 기본 USER 유지(최소 1개 이상의 권한을 가질 것을 보장하는 정책)
        if (user.getUserRoles().isEmpty()) {
            user.grantRole(roleRepository.getReferenceById(RoleType.USER));
        }

//        if (user.getRoles().isEmpty()) {
//            user.addRole(RoleType.USER);
//        }

        RoleManageResponse.RemoveRoleResponse data = new RoleManageResponse.RemoveRoleResponse(
                user.getId(),
                user.getLoginId(),
                req.role(),
                Set.copyOf(user.getRoleTypes()),
                user.getUpdatedAt()
        );
        return ResponseDto.setSuccess("SUCCESS", data);
    }
}