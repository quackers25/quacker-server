package io.quacker.domain.post.dao;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자의 게시글 조회
    List<Post> findByUser(User user);

    // 특정 원본 게시글의 리트윗(공유) 목록 조회
    List<Post> findByOriginPost(Post originPost);

    // 특정 사용자의 최신 게시글 리스트 (최신순 정렬)
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    // 특정 키워드가 포함된 게시글 찾기 (검색 기능)
    @Query("SELECT p FROM Post p WHERE p.text LIKE CONCAT('%', :keyword, '%')")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
}
