package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
