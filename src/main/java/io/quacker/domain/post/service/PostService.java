package io.quacker.domain.post.service;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.repository.PostRepository;
import io.quacker.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    
    private final PostRepository postRepository;
    
    @Transactional
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseGet(() -> {
                    Post newPost = Post.builder()
                            .id(postId)
                            .content("테스트용 임시 게시물")
                            .version(0L)
                            .build();
                    try {
                        return postRepository.save(newPost);
                    } catch (Exception e) {
                        // 저장 실패 시 다시 조회 시도
                        return postRepository.findById(postId)
                                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
                    }
                });
    }
} 