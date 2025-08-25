### k5_iot_springboot >>> query ###

# 1. 스키마 생성 (이미 존재하면 삭제)
DROP DATABASE IF EXISTS k5_iot_springboot;

# 2. 스키마 생성 + 문자셋/정렬 설정
CREATE DATABASE IF NOT EXISTS k5_iot_springboot
	CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
    
# 3. 스키마 선택
USE k5_iot_springboot;

# 0811 (A_Test)
CREATE TABLE IF NOT EXISTS test (
	test_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);
select * FROM test;

# 0812 (B_Student)
CREATE TABLE IF NOT EXISTS students(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    UNIQUE KEY uq_name_email (name, email)
    # : name + email 조합이 유일하도록 설정
);
SELECT * FROM students;

# 0813 (C_Book)
CREATE TABLE IF NOT EXISTS books (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    writer VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    category VARCHAR(20) NOT NULL,
    # 자바 enum 데이터 처리 
    # : DB에서는 VARCHAR(문자열)로 관리 + CHECK 제약 조건으로 문자 제한
    CONSTRAINT chk_book_category CHECK (category IN ('NOVEL', 'ESSAY', 'POEM', 'MAGAZINE')),
    # 같은 저자 + 동일 제목 중복 저장 방지
    CONSTRAINT uk_book_writer_title UNIQUE (writer, title)
);
SELECT * FROM books;

# 0819 (D_Post & D_Comment)
-- 게시글 테이블
CREATE TABLE IF NOT EXISTS `posts` (
	`id`		BIGINT NOT NULL AUTO_INCREMENT,
    `title` 	VARCHAR(200) NOT NULL COMMENT '게시글 제목',
    `content` 	LONGTEXT NOT NULL COMMENT '게시글 내용', -- @Lob 매핑 대응
    `author` 	VARCHAR(100) NOT NULL COMMENT '작성자 표시명 또는 ID',
    PRIMARY KEY (`id`),
    KEY `idx_post_author` (`author`)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '게시글';
  
-- 댓글 테이블
CREATE TABLE IF NOT EXISTS `comments` (
	`id`		BIGINT NOT NULL AUTO_INCREMENT,
    `post_id`	BIGINT NOT NULL COMMENT 'posts.id FK',
    `content` 	varchar(1000) not null comment '댓글 내용',
    `commenter` varchar(100) not null comment '댓글 작성자 표시명 또는 ID',
    primary key (`id`),
    key `idx_comment_post_id` (`post_id`),
    key `idx_comment_commenter` (`commenter`),
    constraint `fk_comment_post`
		foreign key (`post_id`) references `posts` (`id`)
        on delete cascade
        on update cascade
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '댓글';

select * from `posts`;
select * from `comments`;

# 0821 (F_Board)
-- 게시판 테이블(생성/수정 시간 포함)
CREATE TABLE IF NOT EXISTS 	`boards` (
	id BIGINT auto_increment,
    title varchar(150) not null,
    content longtext not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    primary key (`id`)
) engine=InnoDB
default charset = utf8mb4
collate = utf8mb4_unicode_ci
comment = '게시글';

select * from `boards`;

# 0825 (G_Users)
CREATE TABLE IF NOT EXISTS 	`users` (
	id BIGINT NOT NULL AUTO_INCREMENT,
    login_id VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `uk_users_login_id` UNIQUE (login_id),
    CONSTRAINT `uk_users_email` UNIQUE (email),
    CONSTRAINT `uk_users_nickname` UNIQUE (nickname),
    CONSTRAINT `chk_users_gender` CHECK(gender IN('MALE', 'FEMALE'))
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '사용자';
  
SELECT * FROM `users`;

USE k5_iot_springboot;