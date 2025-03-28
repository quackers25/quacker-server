package io.quacker.domain.postlike.entity;

import static org.assertj.core.api.Assertions.assertThat;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostLikeTest {

    private User user;
    private Post post;
    private PostLike postLike;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .email("test@example.com")
            .build();
        post = Post.builder()
            .text("test content")
            .build();
        postLike = PostLike.builder()
            .post(post)
            .user(user)
            .build();
    }

    @Test
    void createPostLikeTest() {
        assertThat(postLike.getUser()).isEqualTo(user);
        assertThat(postLike.getPost()).isEqualTo(post);
    }

    @Test
    void incrementLikeCountTest() {
        post.incrementLikeCount();
        assertThat(post.getLikeCount()).isEqualTo(1);
    }

    @Test
    void decrementLikeCountTest() {
        post.incrementLikeCount();
        post.incrementLikeCount();
        post.decrementLikeCount();
        assertThat(post.getLikeCount()).isEqualTo(1);
    }

    @Test
    void addPostLikeTest() {
        post.addLike(postLike);
        assertThat(post.getLikes()).hasSize(1);
        assertThat(post.getLikes()).contains(postLike);
    }

    @Test
    void removePostLikeTest() {
        post.addLike(postLike);
        post.removeLike(postLike);
        assertThat(post.getLikes()).isEmpty();
    }
} 