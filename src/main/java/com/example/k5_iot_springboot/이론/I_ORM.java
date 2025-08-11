package com.example.k5_iot_springboot.이론;

/*
    === ORM (Object - Relational Mapping) ===
    : 객체와 관계형 DB 간의 1:1 데이터 매핑 기술
    - DB의 테이블과 애플리케이션의 객체 사이의 구조적 불일치를 해결하는 솔루션

    RDBMS(MySQL) - user_password (lower_snake_case): 테이블
    SpringBoot(JAVA) - userPassword (lowerCamelCase): 클래스

    1. ORM 특징
    : 객체와 테이블이 1:1로 매핑
    - 객체지향적인 데이터 조작 (SQL 대신 자바 객체 메서드로 CRUD 작업 수행)
    - 반복적인 SQL 작성 없이, 데이터 조작 가능 (DBMS 독립적)

    <단점> 학습 곡선 존재, 복잡한 쿼리 작성 어려움

    2. ORM 동작 원리
    : 각 테이블은 클래스에, 테이블의 각 행(Row)은 객체에 매핑
    - Entity: DB의 테이블과 매핑되는 클래스 (@Entity)
    - Field: DB의 열(Column)에 해당되는 필드

    3. JPA (Java Persistence API)
    : ORM 중 하나
    - Java 애플리케이션에서 관계형 DB를 쉽게 다룰 수 있는 ORM 표준 기술

    cf) 영속성 컨텍스트
    : 엔티티의 생명주기를 관리하는 공간
    - DB와의 연결은 유지하면서 엔티티 객체들을 관리
*/
public class I_ORM {
}
