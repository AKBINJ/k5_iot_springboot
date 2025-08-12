package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.B_Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface B_StudentRepository extends JpaRepository<B_Student, Long> {
    List<B_Student> findByNameContainingIgnoreCase(String name);

}
