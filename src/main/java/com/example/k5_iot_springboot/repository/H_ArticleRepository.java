package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.H_Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H_ArticleRepository extends JpaRepository<H_Article, Long> {
}
