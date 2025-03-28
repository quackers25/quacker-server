package io.quacker.domain.postlike.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.dto.PostLikeResponse;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postlike.repository.PostLikeRepository;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostLikeService postLikeService;

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
    void toggleLikeTest_WhenNotLiked() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());
        when(postLikeRepository.save(any(PostLike.class))).thenReturn(postLike);

        PostLikeResponse response = postLikeService.toggleLike(1L, 1L);

        assertThat(response.isLiked()).isTrue();
        assertThat(response.likeCount()).isEqualTo(1);
    }

    @Test
    void toggleLikeTest_WhenAlreadyLiked() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(postLike));

        PostLikeResponse response = postLikeService.toggleLike(1L, 1L);

        assertThat(response.isLiked()).isFalse();
        assertThat(response.likeCount()).isEqualTo(0);
    }

    @Test
    void getLikeStatusTest_WhenLiked() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(postLike));

        PostLikeResponse response = postLikeService.getLikeStatus(1L, 1L);

        assertThat(response.isLiked()).isTrue();
        assertThat(response.likeCount()).isEqualTo(0);
    }

    @Test
    void getLikeStatusTest_WhenNotLiked() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

        PostLikeResponse response = postLikeService.getLikeStatus(1L, 1L);

        assertThat(response.isLiked()).isFalse();
        assertThat(response.likeCount()).isEqualTo(0);
    }
} 