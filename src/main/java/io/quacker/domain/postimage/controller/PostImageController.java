package io.quacker.domain.postimage.controller;

import io.quacker.domain.postimage.api.PostImageApi;
import io.quacker.domain.postimage.dto.PostImageDto;
import io.quacker.domain.postimage.service.PostImageService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/images")
public class PostImageController implements PostImageApi {

    private final PostImageService postImageService;

    @GetMapping
    public ResponseEntity<List<PostImageDto>> getImages(
            @PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }

    @PatchMapping("/{imageId}")
    public ResponseEntity<PostImageDto> updateImage(
            @PathVariable Long postId,
            @PathVariable Long imageId,
            @RequestParam @NotBlank String newImageUrl
    ) {
        return ResponseEntity.ok(postImageService.updateImage(imageId, newImageUrl));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long postId,
            @PathVariable Long imageId
    ) {
        postImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
