package io.quacker.domain.post.controller;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 (이미지 포함)", description = "게시글 텍스트와 이미지 파일(최대 4개)을 업로드합니다.")
    public ResponseEntity<PostDto> createPost(@ModelAttribute PostCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(request));
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
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정 (텍스트 수정, 이미지 추가 및 삭제)")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequestDto request
    ) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }


    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
