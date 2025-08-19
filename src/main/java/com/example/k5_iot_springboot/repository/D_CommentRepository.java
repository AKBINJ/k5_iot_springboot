package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.D_Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface D_CommentRepository extends JpaRepository<D_Comment, Long> {
}