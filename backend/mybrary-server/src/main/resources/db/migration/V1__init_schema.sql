-- ============================================================
-- V1__init_schema.sql
-- user-service(USER_DB) + book-service(mybrary) 스키마 통합
-- MySQL 8.0 / InnoDB / utf8mb4
-- 생성 순서: FK 의존 관계를 고려해 부모 테이블을 먼저 생성한다.
-- ============================================================

-- -------------------------------------------------------
-- 1. users
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id                          BIGINT          NOT NULL AUTO_INCREMENT,
    login_id                         VARCHAR(255)    NOT NULL,
    nickname                         VARCHAR(255)    NOT NULL,
    password                         VARCHAR(255)    NOT NULL,
    role                             VARCHAR(50)     NULL,
    social_id                        VARCHAR(255)    NULL        UNIQUE,
    social_type                      VARCHAR(50)     NULL,
    email                            VARCHAR(255)    NULL,
    introduction                     VARCHAR(255)    NULL,
    profile_image_url                VARCHAR(255)    NULL,
    profile_image_thumbnail_tiny_url  VARCHAR(255)   NULL,
    profile_image_thumbnail_small_url VARCHAR(255)   NULL,
    -- BaseEntity
    created_at                       DATETIME(6)     NULL,
    updated_at                       DATETIME(6)     NULL,
    deleted                          TINYINT(1)      NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id),
    -- 복합 유니크 제약 (엔티티 @UniqueConstraint 그대로)
    CONSTRAINT uk_users_login_id_deleted
        UNIQUE (login_id, deleted),
    CONSTRAINT uk_users_nickname_deleted
        UNIQUE (nickname, deleted),
    CONSTRAINT uk_users_login_id_nickname_deleted
        UNIQUE (login_id, nickname, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 2. follows  (FK -> users)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS follows (
    follow_id   BIGINT      NOT NULL AUTO_INCREMENT,
    source_id   BIGINT      NULL,
    target_id   BIGINT      NULL,
    -- BaseEntity
    created_at  DATETIME(6) NULL,
    updated_at  DATETIME(6) NULL,
    deleted     TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (follow_id),
    CONSTRAINT fk_follows_source
        FOREIGN KEY (source_id) REFERENCES users (user_id),
    CONSTRAINT fk_follows_target
        FOREIGN KEY (target_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 3. interest_categories
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS interest_categories (
    interest_category_id  BIGINT       NOT NULL AUTO_INCREMENT,
    name                  VARCHAR(255) NOT NULL UNIQUE,
    description           VARCHAR(255) NOT NULL UNIQUE,
    -- BaseEntity
    created_at            DATETIME(6)  NULL,
    updated_at            DATETIME(6)  NULL,
    deleted               TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (interest_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 4. interests  (FK -> interest_categories)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS interests (
    interest_id           BIGINT       NOT NULL AUTO_INCREMENT,
    interest_category_id  BIGINT       NULL,
    name                  VARCHAR(255) NOT NULL UNIQUE,
    code                  INT          NOT NULL UNIQUE,
    -- BaseEntity
    created_at            DATETIME(6)  NULL,
    updated_at            DATETIME(6)  NULL,
    deleted               TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (interest_id),
    CONSTRAINT fk_interests_category
        FOREIGN KEY (interest_category_id) REFERENCES interest_categories (interest_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 5. users_interests  (FK -> users, interests)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS users_interests (
    user_interest_id  BIGINT     NOT NULL AUTO_INCREMENT,
    user_id           BIGINT     NULL,
    interest_id       BIGINT     NULL,
    -- BaseEntity
    created_at        DATETIME(6) NULL,
    updated_at        DATETIME(6) NULL,
    deleted           TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (user_interest_id),
    CONSTRAINT uk_users_interests_user_id_interest_id
        UNIQUE (user_id, interest_id),
    CONSTRAINT fk_users_interests_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_users_interests_interest
        FOREIGN KEY (interest_id) REFERENCES interests (interest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 6. book_categories
--    @Entity BookCategory는 BaseEntity를 상속하지 않는다.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS book_categories (
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    cid   INT          NULL,
    name  VARCHAR(255) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 7. authors  (BaseEntity 상속)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS authors (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    aid         INT          NULL,
    name        VARCHAR(255) NULL,
    -- BaseEntity
    created_at  DATETIME(6)  NULL,
    updated_at  DATETIME(6)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 8. translators  (BaseEntity 상속)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS translators (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    tid         INT          NULL,
    name        VARCHAR(255) NULL,
    -- BaseEntity
    created_at  DATETIME(6)  NULL,
    updated_at  DATETIME(6)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 9. books  (FK -> book_categories)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS books (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    title               VARCHAR(255)    NULL,
    sub_title           VARCHAR(255)    NULL,
    author              VARCHAR(255)    NULL,
    thumbnail_url       VARCHAR(255)    NULL,
    link                VARCHAR(255)    NULL,
    isbn10              VARCHAR(255)    NULL        UNIQUE,
    isbn13              VARCHAR(255)    NOT NULL    UNIQUE,
    pages               INT             NULL,
    publisher           VARCHAR(255)    NULL,
    publication_date    DATETIME(6)     NULL,
    description         TEXT            NULL,
    toc                 TEXT            NULL,
    weight              INT             NULL,
    size_depth          INT             NULL,
    size_height         INT             NULL,
    size_width          INT             NULL,
    price_sales         INT             NULL,
    price_standard      INT             NULL,
    holder_count        INT             NULL,
    read_count          INT             NULL,
    interest_count      INT             NULL,
    star_rating         DOUBLE          NULL,
    review_count        INT             NULL,
    aladin_star_rating  DOUBLE          NULL,
    aladin_review_count INT             NULL,
    book_category_id    BIGINT          NULL,
    -- BaseEntity
    created_at          DATETIME(6)     NULL,
    updated_at          DATETIME(6)     NULL,
    deleted             TINYINT(1)      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_books_book_category
        FOREIGN KEY (book_category_id) REFERENCES book_categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 10. books_authors  (FK -> books, authors)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS books_authors (
    id          BIGINT     NOT NULL AUTO_INCREMENT,
    book_id     BIGINT     NULL,
    author_id   BIGINT     NULL,
    -- BaseEntity
    created_at  DATETIME(6) NULL,
    updated_at  DATETIME(6) NULL,
    deleted     TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_books_authors_book
        FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_books_authors_author
        FOREIGN KEY (author_id) REFERENCES authors (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 11. books_translators  (FK -> books, translators)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS books_translators (
    id             BIGINT     NOT NULL AUTO_INCREMENT,
    book_id        BIGINT     NULL,
    translator_id  BIGINT     NULL,
    -- BaseEntity
    created_at     DATETIME(6) NULL,
    updated_at     DATETIME(6) NULL,
    deleted        TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_books_translators_book
        FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_books_translators_translator
        FOREIGN KEY (translator_id) REFERENCES translators (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 12. users_interest_books  (FK -> books)
--     BookInterest.userId 는 String (user loginId 참조,
--     users 테이블 FK 없음 — 원본 book-service에서 userId
--     를 헤더로 받아 String으로 저장했던 설계 유지)
--     BookInterest.book 의 @JoinColumn(name = "book") 에 따라
--     FK 컬럼명이 "book" 이다.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS users_interest_books (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     VARCHAR(255) NULL,
    book        BIGINT       NULL,
    -- BaseEntity
    created_at  DATETIME(6)  NULL,
    updated_at  DATETIME(6)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_interest_books__user_id__book
        UNIQUE (user_id, book),
    CONSTRAINT fk_users_interest_books_book
        FOREIGN KEY (book) REFERENCES books (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 13. my_book  (FK -> books)
--     MyBook은 @Table 어노테이션이 없으므로
--     SpringPhysicalNamingStrategy 에 의해 my_book 으로 매핑.
--     MyBook.deleted 는 엔티티에 직접 선언되어 있고
--     BaseEntity.deleted 와 동일 컬럼으로 합쳐진다.
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS my_book (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    user_id                 VARCHAR(255) NULL,
    book_id                 BIGINT       NULL,
    read_status             VARCHAR(50)  NULL,
    start_date_of_possession DATETIME(6) NULL,
    showable                TINYINT(1)   NOT NULL DEFAULT 0,
    exchangeable            TINYINT(1)   NOT NULL DEFAULT 0,
    shareable               TINYINT(1)   NOT NULL DEFAULT 0,
    -- BaseEntity + 엔티티 직접 선언 deleted (동일 컬럼)
    created_at              DATETIME(6)  NULL,
    updated_at              DATETIME(6)  NULL,
    deleted                 TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_my_book_book
        FOREIGN KEY (book_id) REFERENCES books (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 14. meaning_tag  (BaseEntity 상속, @Table 없음 → meaning_tag)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS meaning_tag (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    quote             VARCHAR(255) NULL,
    registered_count  INT          NOT NULL DEFAULT 0,
    created_by        VARCHAR(255) NULL,
    -- BaseEntity
    created_at        DATETIME(6)  NULL,
    updated_at        DATETIME(6)  NULL,
    deleted           TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 15. my_book_meaning_tag  (FK -> my_book, meaning_tag)
--     @Table 없음 → my_book_meaning_tag
--     @OneToOne MyBook → FK: my_book_id
--     @ManyToOne MeaningTag → FK: meaning_tag_id
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS my_book_meaning_tag (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    my_book_id          BIGINT       NULL,
    meaning_tag_id      BIGINT       NULL,
    meaning_tag_color   VARCHAR(255) NULL,
    -- BaseEntity
    created_at          DATETIME(6)  NULL,
    updated_at          DATETIME(6)  NULL,
    deleted             TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_my_book_meaning_tag_my_book
        FOREIGN KEY (my_book_id) REFERENCES my_book (id),
    CONSTRAINT fk_my_book_meaning_tag_meaning_tag
        FOREIGN KEY (meaning_tag_id) REFERENCES meaning_tag (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
-- 16. my_review  (FK -> my_book, books)
--     @Table 없음 → my_review
--     @OneToOne MyBook → FK: my_book_id
--     @ManyToOne Book  → FK: book_id
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS my_review (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    my_book_id   BIGINT       NULL,
    book_id      BIGINT       NULL,
    content      VARCHAR(500) NULL,
    star_rating  DOUBLE       NULL,
    -- BaseEntity + 엔티티 직접 선언 deleted
    created_at   DATETIME(6)  NULL,
    updated_at   DATETIME(6)  NULL,
    deleted      TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_my_review_my_book
        FOREIGN KEY (my_book_id) REFERENCES my_book (id),
    CONSTRAINT fk_my_review_book
        FOREIGN KEY (book_id) REFERENCES books (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
