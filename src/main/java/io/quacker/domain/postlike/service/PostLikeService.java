package io.quacker.domain.postlike.service;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postlike.repository.PostLikeRepository;
import io.quacker.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public PostLike toggleLike(Post post, User user) {
        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);
        
        if (existingLike.isPresent()) {
            PostLike postLike = existingLike.get();
            postLike.unlike();
            postLikeRepository.delete(postLike);
            return null;
        } else {
            return postLikeRepository.save(PostLike.of(post, user));
        }
    }

    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Post post, User user) {
        return postLikeRepository.existsByPostAndUser(post, user);
    }
}
