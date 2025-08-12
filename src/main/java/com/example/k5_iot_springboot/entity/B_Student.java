package com.example.k5_iot_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // DB 테이블과 1:1 매핑, JPA 엔티티임을 선언 (반드시 PK 기본키가 필요)
// : JPA는 엔티티를 통해 객체와 테이블 간 데이터를 자동 변환(ORM)
@Table(name = "students", uniqueConstraints = {
        // 2) 복합 컬럼 제약
        //      : name + email 조합이 동일하면 저장 불가
        @UniqueConstraint(columnNames = {"email", "name"})
})
// name 옵션: 클래스명과 테이블명이 다를 경우 반드시 명시
// cf) 클래스명과 단수 명사, UpperCamelCase
//     DB명 복수 명사, lower_snake_case
@Getter
@Setter
@NoArgsConstructor // JPA는 엔티티 생성 시 기본 생성자가 필수
public class B_Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @RequiredArgsConstructor - final 또는 @NonNull 필드를 매개변수로 사용하는 생성자
    private String name;

    // 엔티티 UNIQUE 제약 조건
    // 1) 컬럼 단위 명시 - 기본값 false
    @Column(unique = true)
    private String email;

    public B_Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
