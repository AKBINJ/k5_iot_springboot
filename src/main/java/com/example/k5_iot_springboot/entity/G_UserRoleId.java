package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.common.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * == 복합키 (user_roles PK) ==
 * : user_id + role_name
 * */
@Embeddable
// 자바 ORM 프레임워크 JPA에서 새로운 값 타입 정의 시 사용하는 어노테이션
// : 특정 엔티티에서 여러 속성들을 하나로 묶어 새로운 복합 값 타입(임베디드 타입)으로 만들 때 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class G_UserRoleId implements Serializable {
    // Serializable 인터페이스
    // : 아무런 내용도 없는 마커 인터페이스
    // - 데이터를 한 시스템에서 다른 시스템이나 네트워크로 전송하거나 데이터를 파일에 저장할 때 사용

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 30, nullable = false)
    private RoleType roleName;

    public G_UserRoleId(Long userId, RoleType roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }
}
