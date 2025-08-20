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

    // ================== 필터링 & 검색 ================== //
    // === 1. 쿼리 메서드(Query Method) ===
    // : Spring Data JPA가 메서드명을 파싱하여 JPQL을 자동 생성

    // EX1) findByAuthorOrderByIdDesc
    //      >> (where author = ?) + (order by id desc)
    // EX2) findByTitleLikeIgnoreCaseOrderByIdDesc
    //      >> (where lower(title) like lower(?)) + (order by id desc)

    List<D_Post> findByAuthorOrderByIdDesc(String author);
    List<D_Post> findByTitleContainingIgnoreCaseOrderByIdDesc(String keyword);
    // +) 8번(댓글이 가장 많은 상위 N개)
    //      : 쿼리 메서드만으로는 집계/정렬 불가! >> JPQL 또는 Native Query 사용

    // === 2. JPQL(@Query) ===
    // : 그룹핑/집계/조인/서브쿼리 등 쿼리메서드로 표현이 어려운 경우 명시적으로 JPQL을 작성
    // - 엔티티명/필드명을 기준으로 작성 (DB에 독립적)
    // 6) 특정 작성자의 모든 게시글 (최신글 우선)
    @Query("""
        SELECT P
        FROM D_Post P
        WHERE P.author = :author
        ORDER BY P.id DESC
    """)
    List<D_Post> findByAuthorOrderByIdDesc_Jpql(@Param("author") String author);

    // === 3. Native SQL ===
    // : DB 벤더 특화 기능(극한 성능이 필요한 경우)
    // - 복잡한 통계/랭킹/리포트에 SQL 가독성이 JPQL보다 뛰어남
    // >> DB를 기준으로 작성

    // +) 매핑 전략
    // [1] 엔티티 반환 - 엔티티 필드와 동일한 별칭으로 모든 컬럼 선택 후 반환
    //      >> 일부 컬럼만 선택할 경우 매핑 실패 OR 지연 로딩 문제 발생

    // [2] 인터페이스 프로젝션 (권장)
    //      >> 결과 컬럼 별칭 <-> 인터페이스 getter 이름 매칭으로 타입 세이프 (캐스팅 불필요)

    // [3] Object 객체로 반환
    //      >> 각 필드별로 형 변환

    // 6) 특정 작성자의 모든 게시글 (최신글 우선)
    @Query(value = """
        select *
        from posts
        where author = :author
        order by id desc 
    """, nativeQuery = true)
    List<D_Post> findByAuthorOrderByIdDesc_Native(@Param("author") String author);

    // == 7) 제목 키워드 검색 (JPQL & Native SQL) == //
    /*
     * SELECT * FROM posts
     * WHERE
     *   title LIKE %keyword%
     * ORDER BY
     *   id DESC
     * */
    @Query("""
        select P
        from D_Post P
        where
            lower(P.title) like lower(concat('%', :keyword, '%'))
        order by P.id desc
    """)
    List<D_Post> searchByTitleKeyword_Jpql(@Param("keyword") String keyword);

    @Query(value = """
        select *
        from posts
        where title like concat('%', :keyword, '%')
        order by id desc 
    """, nativeQuery = true)
    List<D_Post> searchByTitleKeyword_Native(@Param("keyword") String keyword);

    // == 8) 댓글이 가장 많은 상위 N개 (JPQL & Native SQL) == //
    @Query("""
        select P as post, count(C.id) as cnt
        from
            D_Post P
                left join D_Comment C
                on C.post = P
        group by P
        order by cnt desc, P.id desc
    """)
    List<Object[]> findTopPostsByCommentCount_Jpql();
    // Object[]
    // : [0] - D_Post
    // : [1] - 댓글 수 Number

    public interface PostWithCommentCountProjection {
        Long getPostId(); // posts.id
        String getTitle(); // posts.title
        String getAuthor(); // posts.author
        Long getCommentCount(); // count(c.id)
    }

    @Query(value = """
        SELECT 
                p.id as postId,
                p.title as title,
                p.author as author,
                count(c.id) as commentCount
        FROM    
            posts p
            LEFT JOIN comments c 
            ON c.post_id = p.id 
        GROUP BY 
            p.id, p.title, p.author
        ORDER BY 
            commentCount DESC, p.id DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<PostWithCommentCountProjection> findTopPostsByCommentCount_Native(@Param("limit") int limit);

    // 9) 특정 키워드를 포함하는 "댓글"이 달린 게시글 조회
    public interface PostListProjection {
        Long getId();
        String getTitle();
        String getContent();
        String getAuthor();
    }
    @Query(value = """
        SELECT
            P.id        AS id,
            P.title     AS title,
            p.content   AS content,
            P.author    AS author
        FROM
            posts P
                LEFT JOIN comments C
                ON C.post_id = P.id
        WHERE
            LOWER(C.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
        GROUP BY
            P.id, P.title, P.content, P.author
        ORDER BY
            P.id DESC
    """, nativeQuery = true)
    List<PostListProjection> findByCommentKeyword(@Param("keyword") String keyword );

    @Query(value = """
        SELECT
            p.id        AS postId,
            p.title     AS  title,
            p.author    AS  author,
            COUNT(c.id) AS commentCount
        FROM posts p
        LEFT JOIN comments c ON c.post_id = p.id
        WHERE p.author = :author
        GROUP BY p.id, p.title, p.author
        HAVING count(c.id) >= :minCount
        ORDER BY commentCount DESC, p.id DESC
    """, nativeQuery = true)
    List<PostWithCommentCountProjection> findAuthorPostsWithMinCount(
            @Param("author") String author,
            @Param("minCount") int minCount
    );
}