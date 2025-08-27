
package com.example.k5_iot_springboot.security;

import com.example.k5_iot_springboot.entity.G_User;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * === UserPrincipalMapper ===
 * : 도메인 엔티티(G_User) -> 보안 표현(UserPrincipal)로 변환
 * +) 현재 G_User에는 roles가 없으므로 기본 ROLE_USER 부여
 *
 * >> 추후 역할/권한 도입 시 해당 클래스만 변경하면 전역 반영 가능
 *
 * cf) 스프링 시큐리티는 인증/인가 단계에서 UserDetails 인터페이스를 사용 ( >> UserPrincipal)
 *      - 본 매퍼는 영속 엔티티로부터 인증/인가에 꼭 필요한 값만 뽑아
 *          , 경량/불변 VO(UserPrincipal)로 만들어 SecurityContext에 안전하게 전달되도록 하는 매퍼
 *
 * # 사용 위치 #
 * CustomUserDetailsService#loadUserByUsername(...) 가 G_User 조회
 *  -> 본 매퍼로 UserPrincipal 생성
 *  -> Authentication(Principal)에 주입되어 보안 컨텍스트에 저장
 * */
@Component
public class UserPrincipalMapper {

    @NonNull
    public UserPrincipal map(@NonNull G_User user) {
//        Collection<SimpleGrantedAuthority> authorities
//                = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        Collection<? extends GrantedAuthority> authorities =
                // 사용자 정보 내부의 권한이 비어져 있거나 없는 경우
                (user.getRoles() == null || user.getRoles().isEmpty())
                        // 기본 권한 "ROLE_USER" 부여
                        ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        // 해당 권한(들)을 GrantedAuthority 타입으로 변환하여 반환
                        : user.getRoles().stream()
                        .map(r -> {
                            String name = r.name();
                            String role = name.startsWith("ROLE_") ? name : "ROLE_" + name;
                            return new SimpleGrantedAuthority(role);
                        })
                        .toList();

        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getLoginId())
                .password(user.getPassword())
                .authorities(authorities)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }
}
