package io.quacker.domain.postlike.repository;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
} 