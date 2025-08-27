
package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.common.enums.Gender;
import com.example.k5_iot_springboot.common.enums.RoleType;
import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * User 엔티티
 * - 테이블(users)와 1:1 매핑
 * - 생성/수정 시간은 BaseTimeEntity에서 자동 세팅
 * - UserDetails 책임은 분리 (별도 어댑터/매퍼가 담당)
 * */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_login_id", columnNames = "login_id"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 기본 생성자 생성 - 외부 new 방지, JPA 프록시/리플렉션용
// cf) 프록시(proxy): 객체의 대리인 역할, 리플렉션(refelction): 객체의 정보를 동적으로 가져오고 조작하는 기술
public class G_User extends BaseTimeEntity {

    /** PK: 고유 번호 */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    /** 로그인 아이디 (유니크) */
    @Column(name = "login_id", updatable = false, nullable = false, length = 50)
    private String loginId;

    /** 로그인 비밀번호 (해시 저장 권장 - 암호화) */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** 이메일 (유니크) */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /** 닉네임 (유니크) */
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    /** 성별 (선택, NULL 허용) */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    /** 여러 권한 보유 */
    @ElementCollection(fetch = FetchType.LAZY) // JWT에 roles를 저장하는 구조 - LAZY 가능
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_roles_user"))
            ,
            uniqueConstraints = @UniqueConstraint(name = "uk_user_roles", columnNames = {"user_id", "role"})
    )
    @Column(name = "role", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>();

    /** 생성 편의 메서드 */
    @Builder
    private G_User(String loginId, String password, String email, String nickname, Gender gender, Set<RoleType> roles) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.roles = (roles == null || roles.isEmpty()) ? new HashSet<>(Set.of(RoleType.USER)) : roles;
    }

    // === 변경(수정) 메서드 === //
    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfile(String nickname, Gender gender) {
        this.nickname = nickname;
        this.gender = gender;
    }

    public void addRole(RoleType role) { this.roles.add(role); }
    public void removeRole(RoleType role) { this.roles.remove(role); }
}
