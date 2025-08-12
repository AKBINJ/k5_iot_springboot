package com.example.k5_iot_springboot.이론;

/*
    === JpaRepository<E, ID>
    : Spring Data JPA에서 제공하는 기본 인터페이스
    - CRUD와 페이징, 정렬을 포함한 다양한 데이터 엑세스 메서드를 제공
    - 반환타입 메서드명(매개변순);의 추상 메서드 형식

    1. CRUD 메서드
    1) E save(E entity);
        : 새로운 엔티티를 저장하거나, 기존 엔티티를 업데이트
        - 매개변수의 데이터값 중 ID값이 DB에 존재하는 경우 update / 존재하지 않을 경우 insert
        - 저장 또는 수정 후 반영된 데이터를 반환

    2) Optional<E> findById(Id id);
        : 주어진 Id에 해당하는 엔티티를 조회
        >> 결과가 존재하지 않을 경우 NoSuchElementException 발생 위험 있음
            Optional<> 사용 권장 - orElseThrow() 패턴으로 안전 처리

    3) boolean existsById(Id id);
        : 주어진 Id가 존재하는지 확인

    4) List<E> findAll();
        : DB에 있는 모든 엔티티 조회

    5) void deleteById(Id id);
        : 주어진 Id 엔티티를 삭제

    cf) E: 엔티티 클래스 타입 - DB 테이블과 매핑되는 클래스
        id: 엔티티의 기본(PK)와 매핑되는 데이터

    2. 쿼리 메서드
    : 메서드 이름만으로 JPQL(또는 SQL) 쿼리를 자동 생성하는 기능
    - 별도 쿼리 작성 없이 이름 규칙에 따라 Spring Data JPA가 내부에서 해석하여 쿼리를 생성

    cf) JPQL
        : Java Persistence Query Language
        - SQL을 기반으로 한 객체 모델용 쿼리 언어

    1) 쿼리 메서드 기본 구조
    - 보통 find, read, get, query 같은 키워드로 시작(주로 find 사용)
    - 위 키워드 뒤에 By를 붙여 조건을 명시

    변환타입 findBy필드명(필드타입 값);
    반환타입 findBy필드명And다른필드명(필드타입 값1, 필드타입 값2);

    And: And 조건 - findByNameAndCategory
    Or: or 조건 - findByNameOrCategory
    Between: 범위 검색 - findByPriceBetween(값1, 값2)

    Less Than / LessThanEqual: 미만, 이하
    GreaterThan / GreaterThanEqual: 초과, 이상

    IsNull / IsNotNull: Null 여부 확인 - findByDescriptionIsNull

    Like / NotLike: 패턴 매칭

    StartingWith, EndingWith, Containing: ~로 시작하는, ~로 끝나는. ~를 포함하는

    In / NotIn: 포함, 불포함

    OrderBy

    cf) 조건이 복잡하거나 조인이 필요한 경우, 메서드 이름만으로 한계가 존재
        - 직접 쿼리를 작성하거나 가능
        >> @Query
*/

import java.util.List;

class Product {
    private Long id;
    private String name;
    private String category;
    private int price;
}

public interface L_JpaRepository {

    // 가격이 특정 값 이상인 상품 조회
    List<Product> findByPriceGreaterThan(int price);

    // 이름에 특정 문자열이 포함된 상품 조회
    List<Product> findByNameContaining(String name);

    // 카테고리와 가격 범위로 상품 조회
    List<Product> findByCategoryAndPriceBetween(String category, int minPrice, int maxPrice);

    // 가격이 내림차순 정렬
    List<Product> findByCategoryOrderByPriceDesc(String category);
}
