package io.quacker.domain.post.dao;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자의 게시글 조회
    Page<Post> findByUser(User user, Pageable pageable);

    // 특정 키워드가 포함된 게시글 찾기 (대소문자 구분 없이 키워드 검색)
    Page<Post> findByTextContainingIgnoreCase(String keyword, Pageable pageable);
}
