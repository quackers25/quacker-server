-- 테스트는 회원가입 API부터 시작하겠습니다. 

-- 테스트 데이터 추가
INSERT INTO users (id, email, password, name, bio, avatar_image_url, is_locked, is_private, is_verified, created_at, updated_at) 
VALUES (100, 'test@example.com', '$2a$10$mzF0/C3s6xnzWGz1l5GYEOvPQEm.YD.pVHvnsCvhgHsqR1VPBaXzO', '테스트', '테스트 계정입니다.', null, false, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO posts (id, text, user_id, like_count, created_at, updated_at) VALUES
(100, '첫 번째 테스트 포스트입니다.', 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(101, '두 번째 테스트 포스트입니다.', 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, '세 번째 테스트 포스트입니다.', 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 