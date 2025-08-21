package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.F_Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface F_BoardRepository extends JpaRepository<F_Board,Long> {
}
