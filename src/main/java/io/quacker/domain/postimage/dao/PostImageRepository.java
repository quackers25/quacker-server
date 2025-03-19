package io.quacker.domain.postimage.dao;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    // 특정 게시글의 이미지 목록 조회
    List<PostImage> findByPost(Post post);
}
