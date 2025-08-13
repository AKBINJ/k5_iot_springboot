package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.C_Book;
import com.example.k5_iot_springboot.entity.C_Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface C_BookRepository extends JpaRepository<C_Book, Long> {

    // 사용자 쿼리 메서드: 인터페이스의 추상 메서드 구조를 가짐

    List<C_Book> findByTitleContaining(String keyword);
    // >> SQL문 변환) SELECT * FROM books WHERE title LIKE %keyword%;

    List<C_Book> findByCategory(C_Category category);
    // >> SQL문 변환) SELECT * FROM books WHERE category=category;
}
