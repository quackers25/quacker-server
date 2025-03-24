package io.quacker.domain.post.controller;

import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.getAllPosts(sortBy));
    }

    // 특정 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 특정 User ID 게시물 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getPostsByUser(
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.getPostsByUserId(sortBy));
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.searchPosts(keyword, sortBy));
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDto> addPost(@RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(postDto));
    }

    // 리포스트
    @PostMapping("/{postId}")
    public ResponseEntity<PostDto> repost(
            @PathVariable Long postId,
            @RequestBody(required = false) PostDto postDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.repost(postId, postDto != null ? postDto : PostDto.empty()));
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(postId, postDto));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
