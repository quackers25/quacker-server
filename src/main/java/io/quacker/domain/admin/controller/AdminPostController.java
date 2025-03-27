package io.quacker.domain.admin.controller;

import io.quacker.domain.admin.service.AdminService;
import io.quacker.domain.hashtag.service.HashtagService;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/posts")
@RequiredArgsConstructor
public class AdminPostController implements AdminPostsApi{

    private final PostService postService;
    private final AdminService adminService;
    private final HashtagService hashtagService;

    // 태그 게시물 이뢀 삭제
    @Override
    @DeleteMapping("/")
    public ResponseEntity<?> deleteAllHashtaggedPost(
            @RequestParam("hasHashtag") String hashtagName
    ) {
        List<Post> posts = hashtagService.findPostsByHashtag(hashtagName);

//        adminService.deletePosts(posts);
        // 삭제 된 개수 반환?

        return ResponseEntity.status(HttpStatus.OK)
                .body(posts);
    }
}