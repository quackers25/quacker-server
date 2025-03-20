package io.quacker.domain.postlike.service;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postlike.repository.PostLikeRepository;
import io.quacker.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void toggleLike(User user, Post post) {
        postLikeRepository.findByUserAndPost(user, post)
                .ifPresentOrElse(
                    postLike -> {
                        postLike.unlike();
                        postLikeRepository.delete(postLike);
                    },
                    () -> {
                        PostLike postLike = PostLike.of(user, post);
                        postLikeRepository.save(postLike);
                    }
                );
    }

    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(User user, Post post) {
        return postLikeRepository.existsByUserAndPost(user, post);
    }
} 