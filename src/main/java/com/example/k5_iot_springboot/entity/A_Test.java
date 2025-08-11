package com.example.k5_iot_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ORM에서 사용되는 주요 어노테이션
// 1. @Entity
//      : 클래스를 DB 테이블과 매핑할 때 사용
//      - name 옵션 추가
//          : 엔티티 이름을 지정, 테이블명과 클래스명이 다를 경우 명시
//          EX) @Entity(name="test") // 테이블명은 test
@Entity
// 2. @Table
//      : 클래스가 어떤 테이블과 매핑되는지 명시
//      - 생략 시 기본으로 클래스 이름이 테이블 명과 매핑
//      - name 옵션 추가
//          : 테이블명 지정
@Table(name = "test")
@NoArgsConstructor
@Getter
@Setter
public class A_Test {
    @Id
    // 1) @ID
    //      : 기본키 설정 어노테이션 (PK키)
    //      - 필드에 첨부, 옵션 없이 사용 가능
    //      - 다른 어노테이션과 함께 기본키 생성 방식이나 타입 지정 가능
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 2) GeneratedValue
    //      : MySQL의 AUTO_INCREMENT에 맞춰 자동 증가
    @Column(name = "test_id", updatable = false)
    // 3) @Column
    //      : 필드를 특정 테이블의 열과 매핑
    //      - 생략 시 기본으로 필드 이름이 열 이름으로 사용 (lowerCamelCase >> lower_snake_case)
    //      +) 옵션
    //          > name 옵션: 열 이름 지정
    //          > nullable 옵션: 열이 null 값을 허용할지 여부 설정 (기본값: true)
    //          > length 옵션: String 타입의 열 길이를 지정 (기본값: 255)
    //          > updatable 옵션: 열이 수정을 허용할지 여부 설정 (기본값: true)
    //          > unique 옵션: 해당 필드의 값이 유일해야 하는지 여부를 지정 (기본값: false)
    //      >> 각 옵션은 콤마, 로 구분하여 나열
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}

// JPA (ORM, 객체와 RDBMS 연결) VS MyBatis(SQL Mapper, SQL 중심 접근)