package io.quacker.domain.postimage.api;

import io.quacker.domain.postimage.dto.PostImageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 이미지 API", description = "게시글에 등록된 이미지들을 조회, 수정, 삭제하는 API입니다.")
@RequestMapping("/api/v1/posts/{postId}/images")
public interface PostImageApi {

    @Operation(summary = "게시글 이미지 목록 조회", description = "게시글 ID로 등록된 이미지들을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "이미지 목록 조회 성공", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "이미지 목록 예시", value = """
                    [
                      {
                        "id": 10,
                        "imageUrl": "https://quacker-image.s3.ap-northeast-2.amazonaws.com/image1.jpg"
                      },
                      {
                        "id": 11,
                        "imageUrl": "https://quacker-image.s3.ap-northeast-2.amazonaws.com/image2.jpg"
                      }
                    ]
                    """)
    }))
    @GetMapping
    ResponseEntity<List<PostImageDto>> getImages(
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    );

    @Operation(summary = "이미지 URL 수정", description = "특정 이미지의 URL을 새로운 URL로 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 URL 수정 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "수정 성공", value = """
                            {
                              "id": 10,
                              "imageUrl": "https://quacker-image.s3.ap-northeast-2.amazonaws.com/updated.jpg"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "에러 예시", value = """
                            {
                              "message": "Image not found"
                            }
                            """)
            }))
    })
    @PatchMapping("/{imageId}")
    ResponseEntity<PostImageDto> updateImage(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "수정할 이미지 ID") @PathVariable Long imageId,
            @Parameter(description = "새로운 이미지 URL") @RequestParam String newImageUrl
    );

    @Operation(summary = "이미지 삭제", description = "특정 게시글의 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "이미지 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "에러 예시", value = """
                            {
                              "message": "Image not found"
                            }
                            """)
            }))
    })
    @DeleteMapping("/{imageId}")
    ResponseEntity<Void> deleteImage(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "삭제할 이미지 ID") @PathVariable Long imageId
    );
}
