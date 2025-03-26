package io.quacker.domain.post.controller;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 작성, 수정, 조회, 삭제 등의 기능을 제공합니다.")
public class PostController {

    private final PostService postService;

    @Operation(summary = "모든 게시글 조회", description = "정렬 조건에 따라 전체 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @Parameter(description = "정렬 방식 (newest, popular)")
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.getAllPosts(sortBy));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 단일 게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(
            @Parameter(description = "게시글 ID")
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @Operation(summary = "특정 사용자의 게시글 조회", description = "사용자의 게시글을 정렬 기준에 따라 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getPostsByUser(
            @Parameter(description = "정렬 방식 (newest, popular)")
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.getPostsByUserId(sortBy));
    }

    @Operation(summary = "게시글 검색", description = "키워드를 포함하는 게시글을 검색하고 정렬 기준에 따라 결과를 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(
            @Parameter(description = "검색 키워드")
            @RequestParam String keyword,
            @Parameter(description = "정렬 방식 (newest, popular)")
            @RequestParam(required = false, defaultValue = "newest") SortBy sortBy) {
        return ResponseEntity.ok(postService.searchPosts(keyword, sortBy));
    }

    @Operation(summary = "게시글 작성", description = "게시글 텍스트와 이미지 파일(최대 4개)을 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(
            @Parameter(description = "게시글 생성 요청 DTO")
            @ModelAttribute PostCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(request));
    }

    @Operation(summary = "리포스트", description = "기존 게시글을 리트윗합니다.")
    @PostMapping("/{postId}")
    public ResponseEntity<PostDto> repost(
            @Parameter(description = "원본 게시글 ID")
            @PathVariable Long postId,
            @Parameter(description = "선택적 텍스트")
            @RequestBody(required = false) PostDto postDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.repost(postId, postDto != null ? postDto : PostDto.empty()));
    }

    @Operation(summary = "게시글 수정", description = "텍스트 수정 및 이미지 추가/삭제 기능을 포함한 게시글 수정")
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> updatePost(
            @Parameter(description = "수정할 게시글 ID")
            @PathVariable Long postId,
            @Parameter(description = "게시글 수정 요청 DTO")
            @ModelAttribute PostUpdateRequestDto request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "삭제할 게시글 ID")
            @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
