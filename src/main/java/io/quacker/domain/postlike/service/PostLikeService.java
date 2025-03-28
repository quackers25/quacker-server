package io.quacker.domain.postlike.service;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.postlike.dto.PostLikeResponse;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postlike.repository.PostLikeRepository;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeResponse toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        boolean isLiked = postLikeRepository.findByPostAndUser(post, user)
                .map(like -> {
                    postLikeRepository.delete(like);
                    post.decrementRepostCount();
                    return false;
                })
                .orElseGet(() -> {
                    PostLike newLike = PostLike.builder()
                            .post(post)
                            .user(user)
                            .build();
                    postLikeRepository.save(newLike);
                    post.incrementLikeCount();
                    return true;
                });

        return new PostLikeResponse(isLiked, post.getLikeCount());
    }

    public PostLikeResponse getLikeStatus(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        boolean isLiked = postLikeRepository.findByPostAndUser(post, user).isPresent();
        return new PostLikeResponse(isLiked, post.getLikeCount());
    }
} 