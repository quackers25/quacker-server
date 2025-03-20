package io.quacker.domain.postlike.controller;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.dto.PostLikeResponseDto;
import io.quacker.domain.postlike.service.PostLikeService;
import io.quacker.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponseDto> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        var user = customUserDetails.getUser();
        
        // 임시 Post 객체 생성 (하드코딩)
        Post post = Post.builder()
                .id(postId)
                .likeCount(0)
                .content("테스트용 임시 게시물")
                .user(user)  // 게시물 작성자를 현재 사용자로 설정
                .build();
        
        postLikeService.toggleLike(user, post);
        
        return ResponseEntity.ok(new PostLikeResponseDto(
            postLikeService.hasUserLikedPost(user, post),
            post.getLikeCount()
        ));
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponseDto> getLikeStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        var user = customUserDetails.getUser();
        
        // 임시 Post 객체 생성 (하드코딩)
        Post post = Post.builder()
                .id(postId)
                .likeCount(0)
                .content("테스트용 임시 게시물")
                .user(user)  // 게시물 작성자를 현재 사용자로 설정
                .build();
        
        return ResponseEntity.ok(new PostLikeResponseDto(
            postLikeService.hasUserLikedPost(user, post),
            post.getLikeCount()
        ));
    }
} 