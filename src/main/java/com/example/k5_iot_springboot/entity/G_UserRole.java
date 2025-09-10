package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * == 사용자-권한 매핑 엔티티 (user_roles) ==
 * */
@Entity
@Table(name = "user_roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class G_UserRole {
    @EmbeddedId
    private G_UserRoleId id;

    /** 복합키의 userId를 G_User의 PK에 매핑*/
    @MapsId("userId")
    // 해당 필드와 연관된 FK(user_id) 값이 해당 엔티티의 식별자(PK)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_roles_user")
    )
    private G_User user;

    /** 복합키의 role_name를 G_Role의 PK에 매핑*/
    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_name",
            nullable = false,
            referencedColumnName = "role_name", // 대상 엔티티의 PK가 아닌 컬럼을 FK로 참조
            foreignKey = @ForeignKey(name = "fk_user_roles_role")
    )
    private G_Role role;

    public G_UserRole(G_User user, G_Role role) {
        this.user = user;
        this.role = role;

        Long userId = user.getId();
        RoleType roleName = role.getName();
        this.id = new G_UserRoleId(userId, roleName);
    }
}
