package com.example.k5_iot_springboot.security.util;

import com.example.k5_iot_springboot.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;

public class PrincipalUtils {
    private PrincipalUtils() {}

    /** UserPrincipal 전용 검증 */
    public static void requiredActive(UserPrincipal principal)  {
        if (principal == null) {
            throw new AccessDeniedException("인증 필요");
        }
        if (!principal.isAccountNonLocked() || !principal.isEnabled() || !principal.isAccountNonExpired()) {
            throw new AccessDeniedException("비활성화 된 계정");
        }
    }
}