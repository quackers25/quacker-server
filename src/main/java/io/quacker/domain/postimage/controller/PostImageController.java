package io.quacker.domain.postimage.controller;

import io.quacker.domain.postimage.dto.PostImageDto;
import io.quacker.domain.postimage.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/images")
@RequiredArgsConstructor
public class PostImageController {

    private final PostImageService postImageService;

    // 특정 게시물에 이미지 조회
    @GetMapping
    public ResponseEntity<List<PostImageDto>> getImagesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }

    // 이미지 url 수정
    @PatchMapping("/{imageId}")
    public ResponseEntity<PostImageDto> updateImage(
            @PathVariable Long imageId,
            @RequestParam String newImageUrl) {
        PostImageDto updatedImage = postImageService.updateImage(imageId, newImageUrl);
        return ResponseEntity.ok(updatedImage);
    }

    // 이미지 삭제
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        postImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
