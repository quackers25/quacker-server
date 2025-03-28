package io.quacker.domain.admin.controller;

import io.quacker.domain.admin.controller.api.AdminPostsApi;
import io.quacker.domain.admin.service.AdminService;
import io.quacker.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/v1/admins/posts")
@RequiredArgsConstructor
public class AdminPostController implements AdminPostsApi {

    private final PostService postService;
    private final AdminService adminService;

    // 태그 게시물 이괄 삭제
    @Override
    @PreAuthorize("ROLE_ADMIN")
    @DeleteMapping("")
    public ResponseEntity<?> deleteAllHashtaggedPost(
            @RequestParam("hasHashtag") String hashtagName
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("count",adminService.deletePostByHahstag(hashtagName)));
    }
}