package com.example.k5_iot_springboot.repository;

import com.example.k5_iot_springboot.entity.D_Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Post와 Comment의 관계가 1:N의 관계
 *
 * D_Post post = postRepository.findById(id).get();
 * post.getComments.forEach(...); // 댓글 접근
 *
 * == 코드 풀이 ==
 * 1) 첫 번째 쿼리: SELECT * FROM posts WHERE id=?
 * 2) 두 번째 쿼리: LAZY 설정 코드를 "여러 번" 실행할 때마다 초기화를 위한 SELECT문이 별도로 실행
 *
 * ## 상황 1) 단일 Post만 조회하는 경우 ##
 * -- 1번째 쿼리
 *   SELECT * FROM posts WHERE id=?
 * -- 2번째 쿼리: 이후 post.getComments() 처음 호출 시
 *   댓글 컬렉션 초기화용으로 딱 1번 실행
 *   SELECT * FROM comments where post_id=?
 *
 * ## 상황 2) Post를 N개 먼저 가져온 뒤 각 Post마다 getComments() 호출
 * -- 1번째 쿼리
 *   SELECT * FROM posts limit 20;
 * -- 2번째 쿼리
 *   SELECT * FROM comments where post_id=? (총 20번 실행)
 *
 * 1번째 쿼리(1) + 2번째 쿼리(N)
 * >> 1+N 문제 발생
 * */

@Repository
public interface D_PostRepository extends JpaRepository<D_Post, Long> {
    // 게시글 조회 + 댓글까지 즉시 로딩

    // 댓글까지 즉시 로딩
    @Query("""
        select distinct p
        from D_Post p
            left join fetch p.comments c
        where p.id = :id
    """)
    Optional<D_Post> findByIdWithComments(@Param("id") Long id);

    // 전체 조회(댓글 제외)
    @Query("""
        select p
        from D_Post p
        order by p.id desc
    """)
    List<D_Post> findAllOrderByIdDesc();
}