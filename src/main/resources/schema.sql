DROP TABLE IF EXISTS post_likes;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    avatar_image_url VARCHAR(255),
    is_locked BOOLEAN DEFAULT FALSE,
    is_private BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text TEXT,
    user_id BIGINT NOT NULL,
    like_count INT DEFAULT 0,
    repost_count INT DEFAULT 0,
    origin_post_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (origin_post_id) REFERENCES posts(id)
);

CREATE TABLE post_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (post_id, user_id)
); 