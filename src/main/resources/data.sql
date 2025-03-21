-- 테스트용 사용자 데이터
INSERT INTO users (email, password, name, created_at, updated_at, is_locked, is_verified, is_private)
VALUES ('test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU7VxW6', '테스트유저', 
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), false, false, false);

-- 테스트용 게시물 데이터
INSERT INTO posts (content, like_count, repost_count, version, created_at, updated_at)
VALUES ('테스트 게시물 1', 0, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       ('테스트 게시물 2', 0, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       ('테스트 게시물 3', 0, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()); 