package io.quacker.domain.post.controller;

import io.quacker.domain.post.api.PostApi;
import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController implements PostApi {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(sortBy, pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/user/{userId}")

    public ResponseEntity<Page<PostDto>> getPostsByUser(
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByUserId(sortBy, pageable));

    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(postService.searchPosts(keyword, sortBy, pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(
            @ModelAttribute PostCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(request));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<PostDto> repost(
            @PathVariable Long postId,
            @RequestBody(required = false) PostDto postDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.repost(postId, postDto != null ? postDto : PostDto.empty()));
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequestDto request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
