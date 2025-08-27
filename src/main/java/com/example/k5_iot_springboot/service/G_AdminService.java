
package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.dto.G_Admin.request.RoleManageRequest;
import com.example.k5_iot_springboot.dto.G_Admin.response.RoleManageResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.security.UserPrincipal;
import jakarta.validation.Valid;

// Admin의 권한 기능만 담은 비즈니스 로직 - UserRoleCommandService
public interface G_AdminService {
    ResponseDto<RoleManageResponse.UpdateRolesResponse> replaceRoles(UserPrincipal principal, RoleManageRequest.@Valid UpdateRolesRequest req);
    ResponseDto<RoleManageResponse.AddRoleResponse> addRole(UserPrincipal principal, RoleManageRequest.@Valid AddRoleRequest req);
    ResponseDto<RoleManageResponse.RemoveRoleResponse> removeRole(UserPrincipal principal, RoleManageRequest.@Valid RemoveRoleRequest req);
}
