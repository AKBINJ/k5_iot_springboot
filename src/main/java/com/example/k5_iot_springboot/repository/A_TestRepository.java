package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.A_Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// A extends B(확장하다): A와 B의 형태가 동일
//      >> 클래스 - 클래스, 인터페이스 - 인터페이스

// C implements D(구현하다): C는 무조건 클래스, D는 무조건 인터페이스

// === Repository(저장소) === //
// : DB에 접근하는 객체 (DAO 형태)
// - DB에 데이터를 일고 쓰는 CRUD 담당 계층
// - JpaRepository를 상속받음 <엔티티타입, PK타입> 형태로 현결할 테이블 명시

// cf) Entity는 테이블 자체를 1:1로 매핑
//      Repository는 Entity 테이블에 CRUD 작업을 수행

@Repository
public interface A_TestRepository extends JpaRepository<A_Test, Long> {
    // 기본 CRUD가 내장
}
