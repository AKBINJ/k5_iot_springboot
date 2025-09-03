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

# 0827 (G_User_role)
-- 사용자 권한 테이블
CREATE TABLE IF NOT EXISTS 	`user_roles` (
	user_id bigint not null,
    role varchar(30) not null,
    
    constraint fk_user_roles_user
		foreign key (user_id) references users(id) on delete cascade,
	constraint uk_user_roles unique (user_id, role),
    
    constraint chk_user_roles_role check (role in ('USER', 'MANAGER', 'ADMIN'))
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '사용자 권한';
  
select * from `user_roles`;

# 샘플데이터 #
INSERT INTO user_roles (user_id, role)
VALUES (1, "ADMIN");
INSERT INTO user_roles (user_id, role)
VALUES (2, "ADMIN");

# 0828 (H_Article)
-- 기사 테이블
CREATE TABLE IF NOT EXISTS 	`articles` (
	id BIGINT AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_article_author
		FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '기사글';
  
select * from articles;

# 0901 (주문 관리 시스템)
-- 트랜잭션, 트리거, 인덱스, 뷰 학습
# products(상품), stocks(재고)
# , orders(주문 정보), order_items(주문 상세 정보), order_logs(주문 기록 정보)

-- 안전 실행: 삭제 순서
# cf) FOREIGN_KEY_CHECKS: 왜래 키 제약 조건을 활성화(1)하거나 비활성화(0)하는 명령어
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS order_logs;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS products;
SET FOREIGN_KEY_CHECKS = 1;

-- 상품 정보 테이블
CREATE TABLE IF NOT EXISTS `products` (
	id 		BIGINT AUTO_INCREMENT PRIMARY KEY,
    name 	VARCHAR(100) NOT NULL,
    price 	INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT uq_product_name UNIQUE (name),
    INDEX idx_product_name (name)				# 제품명으로 제폼 조회 시 성능 향상
) ENGINE=InnoDB									# MySQL에서 테이블이 데이터를 저장하고 관리하는 방식을 지정
  DEFAULT CHARSET = utf8mb4						# DB나 테이블의 기본 문자 집합 (4바이트까지 지원 - 이모지 포함)
  COLLATE = utf8mb4_unicode_ci					# 정렬 순서 지정 (대소문자 구분 없이 문자열 비교 정렬)
  COMMENT = '상품 정보';
  
# cf) ENGINE=InnoDB: 트랜잭션 지원(ACID), 왜래 키 제약조건 지원 (참조 무결성 보장)

CREATE TABLE IF NOT EXISTS `stocks` (
	id			bigint auto_increment primary key,
    product_id	bigint not null,
    quantity	int not null,
    created_at	datetime(6) not null,
    updated_at	datetime(6) not null,
    constraint fk_stocks_product
		foreign key (product_id) references products(id) on delete cascade,	# foreign key
	constraint chk_stocks_qty check (quantity >= 0),						# check 제약 조건
    index idx_stocks_product_id (product_id)								# Index 제약 조건
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '상품 재고 정보';
  
CREATE TABLE IF NOT EXISTS `orders` (
	id				bigint auto_increment primary key,
    user_id			bigint not null,
    order_status 	varchar(50) not null default 'PENDIGN',
    created_at 		datetime(6) not null,
	updated_at		datetime(6) not null,
    constraint fk_orders_user
		foreign key (user_id) references users(id) on delete cascade,
	constraint chk_oders_os check (order_status in ('PENDING', 'APPROVED', 'CANCELLED' )),
    INDEX idx_orders_users (user_id),
    index idx_orders_status (order_status),
    index idx_orders_created_at (created_at)
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '주문 정보';
  
CREATE TABLE IF NOT EXISTS `order_items` (
	id				bigint auto_increment primary key,
    order_id		bigint not null,					# 주문 정보
    product_id		bigint not null,					# 제품 정보
    quantity 		int not null,
	created_at 		datetime(6) not null,
	updated_at		datetime(6) not null,
    constraint fk_order_items_order
		foreign key (order_id) references orders (id) on delete cascade,
	constraint fk_order_items_product
		foreign key (product_id) references products (id) on delete cascade,
	constraint chk_order_items_qty check (quantity > 0),
    index idx_order_items_order (order_id),
    index idx_order_items_product (product_id),
    unique key uq_order_product (order_id, product_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '주문 상세 정보';
  
CREATE TABLE IF NOT EXISTS `order_logs` (
	id			bigint auto_increment primary key,
    order_id	bigint not null,
    message		varchar(225),
    -- 트리거가 직접 insert 하는 로그 테이블은 시간 누락 방지를 위해 DB 기본값 유지
	created_at 	datetime(6) not null default current_timestamp(6),
	updated_at	datetime(6) not null default current_timestamp(6) on update current_timestamp(6),
    constraint fk_order_logs_order
		foreign key (order_id) references orders(id) on delete cascade,
	index idx_order_logs_order (order_id),
    index idx_order_logs_created_at (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '주문 기록 정보';
  
##### 초기 데이터 설정 #####
insert into products (name, price, created_at, updated_at)
values
	('갤럭시 z플립 7', 50000, now(6), now(6)),
	('아이폰 16', 60000, now(6), now(6)),
	('갤럭시 s25 울트라', 55000, now(6), now(6)),
	('맥북 프로 14', 80000, now(6), now(6));
    
insert into stocks (product_id, quantity, created_at, updated_at)
values
	(1, 50, now(6), now(6)),
	(2, 30, now(6), now(6)),
	(3, 70, now(6), now(6)),
	(4, 20, now(6), now(6));
   
### 0902
-- 뷰 (행 단위)
-- : 주문 상세 화면(API) - 한 주문의 각 상품 라인 아이템 정보를 상세하게 제공할 때
-- : 예) GET /api/v1/orders/{odrderId}/items
create or replace view order_summary as
select
	o.id					AS order_id,
    o.user_id				AS user_id,
    o.order_status			AS order_status,
    p.name					AS product_name,
    oi.quantity				AS quantity,
    p.price					AS price,
    CAST((oi.quantity * p.price)AS signed) AS total_price, -- BIGINT로 고정
    o.created_at			AS ordered_at
from
	orders o
    join order_items oi on o.id = oi.order_id
    join products p on oi.product_id = p.id;
    
-- 뷰 (주문 합계)
create or replace view order_totals AS
select
	o.id										AS order_id,
    o.user_id									AS user_id,
    o.order_status								AS order_status,
    cast(SUM(oi.quantity * p.price)as signed) 	AS order_total_amount, -- BIGINT로 고정
    cast(sum(oi.quantity)as signed)				AS order_total_qty, -- BIGINT로 고정
    min(o.created_at)							AS ordered_at

from
	orders o
    join order_items oi on o.id = oi.order_id
    join products p on oi.product_id = p.id
group by
	o.id, o.user_id, o.order_status; -- 주문 별 합계: 주문(orders) 정보를 기준으로 그룹화
    
-- 트리거: 주문 생성 시 로그
# 고객 문의/장애 분석 시 "언제 주문 레코드가 생겼는지" 원인 추적에 사용
DELIMITER //
create trigger trg_after_order_insert
	after insert on orders
    for each row
    begin
		insert into order_logs(order_id, message)
        values (new.id, concat('주문이 생성되었습니다. 주문 ID: ', new.id));
END //
DELIMITER ;

-- 트리거: 주문 상태 변경 시 로그
# 상태 전이 추적 시 "누가 언제 어떤 상태로 바꿨는지" 원인 추적에 사용
DELIMITER //
create trigger trg_after_order_status_update
after update on orders
for each row
begin
	if new.order_status <> OLD.order_status THEN -- A <> B 는 A != B와 같은 의미 (같지 않다)
		insert into order_logs(order_id, message)
        values (new.id, concat('주문 상태가', old.order_status
					, '->', new.order_status, '로 변경되었습니다.'));
	end if;
end //
DELIMITER ;
  
select * from `products`;
select * from `stocks`;
select * from `orders`;
select * from `order_items`;
select * from `order_logs`;

USE k5_iot_springboot;