package io.quacker.domain.postlike.controller;

import io.quacker.domain.postlike.dto.PostLikeResponse;
import io.quacker.domain.postlike.service.PostLikeService;
import io.quacker.domain.user.dto.CustomUserDetails;
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
    public ResponseEntity<PostLikeResponse> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PostLikeResponse response = postLikeService.toggleLike(postId, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<PostLikeResponse> getLikeStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PostLikeResponse response = postLikeService.getLikeStatus(postId, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }
} 