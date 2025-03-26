package io.quacker.domain.postimage.controller;

import io.quacker.domain.postimage.dto.PostImageDto;
import io.quacker.domain.postimage.service.PostImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/images")
@Tag(name = "게시글 이미지 API", description = "게시글에 등록된 이미지를 조회, 수정, 삭제하는 API입니다.")
public class PostImageController {

    private final PostImageService postImageService;

    @Operation(summary = "게시글 이미지 목록 조회", description = "게시글 ID로 등록된 이미지들을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostImageDto>> getImages(
            @Parameter(description = "게시글 ID") @PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }

    @Operation(summary = "이미지 URL 수정", description = "특정 이미지의 URL을 새로운 URL로 수정합니다.")
    @PatchMapping("/{imageId}")
    public ResponseEntity<PostImageDto> updateImage(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "수정할 이미지 ID") @PathVariable Long imageId,
            @Parameter(description = "새로운 이미지 URL") @RequestParam @NotBlank String newImageUrl
    ) {
        return ResponseEntity.ok(postImageService.updateImage(imageId, newImageUrl));
    }

    @Operation(summary = "이미지 삭제", description = "게시글에 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "삭제할 이미지 ID") @PathVariable Long imageId
    ) {
        postImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
