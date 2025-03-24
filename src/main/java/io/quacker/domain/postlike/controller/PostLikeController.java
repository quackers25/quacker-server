package io.quacker.domain.postlike.controller;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postlike.dto.PostLikeResponseDto;
import io.quacker.domain.postlike.service.PostLikeService;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
import io.quacker.global.security.CustomUserDetails;
import io.quacker.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final UserService userService;
    private final PostService postService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponse> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userService.getUser(userDetails.getUser().getId());
        Post post = postService.getPost(postId);
        
        PostLike postLike = postLikeService.toggleLike(post, user);
        boolean isLiked = postLike != null;
        
        return ResponseEntity.ok(new PostLikeResponse(isLiked, post.getLikeCount()));
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponse> getLikeStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userService.getUser(userDetails.getUser().getId());
        Post post = postService.getPost(postId);
        
        boolean isLiked = postLikeService.hasUserLikedPost(post, user);
        return ResponseEntity.ok(new PostLikeResponse(isLiked, post.getLikeCount()));
    }
}

record PostLikeResponse(boolean isLiked, int likeCount) {} 