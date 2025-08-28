package com.example.k5_iot_springboot.entity;

import com.example.k5_iot_springboot.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 매개변수가 없는 기본 생성자를 Protected 접근 제어자로 생성
// : 외부 클래스에서(엔티티 정의 부분 외) 객체를 직접 생성하는 것을 방지
// - 무분별한 객체 생성을 억제
// cf) JPA에서 엔티티 생성 시 기본 생성자가 반드시 필요
//      >> JPA를 위한 생성자를 프로젝트 전역에서 접근하는 것을 방지
public class H_Article extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @JdbcTypeCode(SqlTypes.LONGVARCHAR) // LONGTEXT 타입과 호환
    @Column(nullable = false)
    private String content;

    /** 작성자 (작성자:게시글 1:N) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // fetch: 가져오다, LAZY: 게으른
    // - 연관된 엔티티를 필요한 때만 DB에서 로딩
    @JoinColumn(name = "author_id",
        foreignKey = @ForeignKey(name = "fk_articles_author"))
    private G_User author;

    private H_Article(String title, String content, G_User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static H_Article create(String title, String content, G_User author) {
        return new H_Article(title, content, author);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
