package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.F_Board;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface F_BoardRepository extends JpaRepository<F_Board,Long> {
    // Cursor기반: id 기준 내림차순 "더 작은 id"를 size 만큼 가져오기
    Slice<F_Board> findByIdLessThanOrderByIdDesc(long startId, PageRequest pageable);
}
