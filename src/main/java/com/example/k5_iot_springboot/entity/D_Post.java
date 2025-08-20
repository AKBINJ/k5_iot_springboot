package com.example.k5_iot_springboot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// access = AccessLevel.PROTECTED
// : JPA 프록시 생성을 위한 기본 생성자
// : 외부에서 무분별하게 생성하지 못하도록 접근 수준을 PROTECTED로 제한
@AllArgsConstructor
@ToString(exclude = "comments")
// exclude = "comments"
// : 해당 속성값의 필드를 제외하고 ToString 메서드 내에서 필드값 출력
@Builder
public class D_Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("게시글 제목") // RDBMS 컬럼에 대한 주석 첨부
    @Column(nullable = false, length = 200)
    private String title;

    @Comment("게시글 내용")
    @Lob // 대용량 텍스트 저장 - RDBMS에서 자동으로 TEXT(CLOB) 으로 매핑
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR) // Hibernate에게 LONGTEXT(LONGVARCHAR)로 기대할 것을 명시
    private String content;

    @Comment("작성자 표시명 또는 ID")
    @Column(nullable = false, length = 100)
    private String author;

    // @OneToMany
    // - Post : Comment = 1 : N 관계에서 '1'쪽 매핑임을 설정
    // - 컬렉션 타입은 기본 Lazy 설정
    // >> 해당 어노테이션 내에서 세부 옵션을 지정
    @OneToMany(
            mappedBy = "post",
            // : 관계의 주인은 D_Comment.post 필드임을 지정
            // : Post 내부에서는 읽기 전용 매핑, FK는 D_Comment과 연결된 테이블이 가짐
            cascade = CascadeType.ALL,
            // : 부모(D_Post)에 대한 persist/merge/remove 등이 자식(D_Comment)로 전이
            // - 게시글 저장/삭제 시 댓글도 같이 처리
            orphanRemoval = true,
            // : 컬렉션에서 제거된 댓글은 고아 객체로 간주되어, DB에서도 자동 삭제 (실제 DELETE 수행)
            fetch = FetchType.LAZY
            // : 컬렉션을 지연 로딩
            // - 댓글이 필요할 때만 실제 SELECT를 수행하여 불필요한 로딩을 방지
    )
    private List<D_Comment> comments = new ArrayList<>();
    // - 1 : N 관계 시 컬렉션은 NPE 방지를 위해 즉시 초기화
    // - JPA가 내부적으로 컬렉션 프록시로 교체 가능
    // cf) 프록시 (중개자)

    // === 생성/수정 메서드 === //
    // : 엔티티는 @Setter 제거
    // - 의도된 변경 메서드만 공개
    private D_Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static D_Post create(String title, String content, String author) {
        return new D_Post(title, content, author);
    }

    public void changeTitle(String title) { this.title = title; }
    public void changeContent(String content) { this.content = content; }

    // === 양방향 연관관계 편의 메서드(중복 방지 포함) === //
    /*
     * 연관 관계 메서드
     * : Comment를 Post에 추가/삭제할 때 사용
     * - comments 리스트에 추가/삭제
     *   >> 해당 Comment의 post 필드에 현재 Post 객체를 설정
     * - 해당 설정을 하지 않으면, JPA의 영속성 컨텍스트가 양방향 관계를 완전히 이해하지 못함!
     *
     * cf) 영속성 컨텍스트
     *   : JPA에서 엔티티 객체를 연구, 저장하는 환경을 의미
     *   - 엔티티 매니저에 의해 관리, save(), remove(), find()와 같은 작업을 수행
     *   - 영속성 컨텍스트에 저장된 엔티티는 1차 캐시에 보관
     *       >> 트랜잭션이 끝날 때 실제 DB에 반영
     *
     * cf) 양방향 관계
     *   : 두 엔티티가 서로 참조하는 관계를 의미
     *   - Post가 여러 개의 Comment를 가짐 (@OneToMany)
     *   - Comment가 하나의 Post에 속함 (@ManyToOne)
     * */
    public void addComment(D_Comment comment) {
        if (comment == null) return;
        if (!this.comments.contains(comment)) {
            // : 기존의 댓글 배열(comments)에 추가하려는 댓글(comment)이 포함되어 있지 않은 경우
            this.comments.add(comment);
            comment.setPost(this); // Comment 내에도 게시글 정보 저장
        }
    }

    public void removeComment(D_Comment comment) {
        if (comment == null) return;
        if (this.comments.remove(comment)) {
            comment.setPost(null);
        }
    }
}