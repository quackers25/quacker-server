package io.quacker.domain.post.dao;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 모든 게시물 조회
    List<Post> findAll(Sort sort);

    // 특정 사용자의 게시글 조회
    List<Post> findByUser(User user, Sort sort);
    
    // 특정 사용자의 게시글 페이징 조회
    Page<Post> findByUser(User user, Pageable pageable);

    // 특정 키워드가 포함된 게시글 찾기 (대소문자 구분 없이 키워드 검색)
    List<Post> findByTextContainingIgnoreCase(String keyword, Sort sort);
    
    // 피드 조회 (팔로우한 사용자들의 게시글)
    @Query("SELECT p FROM Post p WHERE p.user IN (SELECT f.followingUser FROM UserFollowing f WHERE f.followerUser = :user) ORDER BY p.createdAt DESC")
    Page<Post> findFeedByUser(@Param("user") User user, Pageable pageable);
}
