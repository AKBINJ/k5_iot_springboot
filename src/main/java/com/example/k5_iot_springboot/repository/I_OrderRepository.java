package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.common.enums.OrderStatus;
import com.example.k5_iot_springboot.entity.I_Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 인터페이스 간의 확장 ("I_OrderRepository 인터페이스" extends "JpaRepository 인터페이스")
@Repository
public interface I_OrderRepository extends JpaRepository<I_Order, Long>, I_OrderRepositoryCustom {

    /** 주문 상세 (주문 - 항목 - 상품) fetch join 단건 조회 */
    @Query("""
        select distinct o from I_Order o
            left join fetch o.items oi
            left join fetch oi.product p
        where o.id = :orderId
    """)
    Optional<I_Order> findDetailById(@Param("orderId") Long orderId);
}