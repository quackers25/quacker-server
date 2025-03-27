package io.quacker.domain.post.api;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글 API", description = "게시글 작성, 조회, 수정, 삭제 기능을 제공합니다.")
@RequestMapping("/api/v1/posts")
public interface PostApi {

    @Operation(summary = "게시글 목록 조회", description = "정렬 및 페이징 정보를 받아 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "게시글 목록 예시", value = """
                                        {
                                          "content": [
                                            {
                                              "id": 1,
                                              "text": "게시글 내용",
                                              "likeCount": 15,
                                              "repostCount": 3,
                                              "user": {
                                                "userId": 1,
                                                "name": "홍길동",
                                                "bio": "백엔드 개발자",
                                                "profileImageUrl": "https://example.com/profile.jpg"
                                              },
                                              "imageUrls": []
                                            }
                                          ],
                                          "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 10
                                          },
                                          "totalPages": 1,
                                          "totalElements": 1
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (정렬 파라미터가 유효하지 않음)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "정렬 값 오류", value = """
                                        {
                                          "message": "지원하지 않는 정렬 방식입니다. (newest 또는 likes)"
                                        }
                                    """)
                    })
            )
    })
    @GetMapping
    ResponseEntity<Page<PostDto>> getAllPosts(
            @Parameter(description = "정렬 방식 (newest, likes)", example = "newest")
            @RequestParam(defaultValue = "newest") SortBy sortBy,

            @ParameterObject Pageable pageable
    );

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 ID를 기반으로 특정 게시글의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "게시글 조회 예시",
                                            value = """
                                                        {
                                                          "id": 10,
                                                          "text": "오늘은 날씨가 좋아요.",
                                                          "likeCount": 12,
                                                          "repostCount": 3,
                                                          "user": {
                                                            "userId": 2,
                                                            "name": "홍길동",
                                                            "bio": "백엔드 개발자",
                                                            "profileImageUrl": "https://example.com/profile.jpg"
                                                          },
                                                          "originPost": null,
                                                          "imageUrls": [
                                                            "https://example.com/image1.jpg",
                                                            "https://example.com/image2.jpg"
                                                          ]
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "해당 ID의 게시글이 존재하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "게시글 조회 실패",
                                            value = """
                                                        {
                                                          "message": "게시글을 찾을 수 없습니다."
                                                        }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<PostDto> getPost(
            @Parameter(description = "게시글 ID", example = "10")
            @PathVariable Long postId
    );

    @Operation(
            summary = "사용자 게시글 목록 조회",
            description = "현재 로그인한 사용자의 게시글 목록을 정렬 및 페이징 정보를 통해 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "사용자 게시글 목록 예시", value = """
                                        {
                                          "content": [
                                            {
                                              "id": 1,
                                              "text": "사용자 게시글 내용",
                                              "likeCount": 8,
                                              "repostCount": 2,
                                              "user": {
                                                "userId": 1,
                                                "name": "홍길동",
                                                "bio": "백엔드 개발자",
                                                "profileImageUrl": "https://example.com/profile.jpg"
                                              },
                                              "imageUrls": []
                                            }
                                          ],
                                          "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 10
                                          },
                                          "totalPages": 1,
                                          "totalElements": 1
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 정렬 파라미터",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "정렬 방식 오류", value = """
                                        {
                                          "message": "지원하지 않는 정렬 방식입니다. (newest 또는 likes)"
                                        }
                                    """)
                    })
            )
    })
    @GetMapping
    ResponseEntity<Page<PostDto>> getPostsByUser(
            @Parameter(description = "정렬 방식 (newest, likes)", example = "likes")
            @RequestParam(defaultValue = "newest") SortBy sortBy,
            @ParameterObject Pageable pageable
    );


    @Operation(
            summary = "게시글 검색",
            description = "키워드를 포함하는 게시글을 정렬 방식과 페이징 기준으로 검색합니다.<br>" +
                    "`keyword`는 필수이며, 정렬 방식은 `newest` 또는 `likes` 중 선택할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "검색 결과 예시", value = """
                                        {
                                          "content": [
                                            {
                                              "id": 5,
                                              "text": "이 게시글에는 검색 키워드가 포함되어 있습니다.",
                                              "likeCount": 10,
                                              "repostCount": 1,
                                              "user": {
                                                "userId": 2,
                                                "name": "유저이름",
                                                "bio": "설명",
                                                "profileImageUrl": "https://example.com/profile.jpg"
                                              },
                                              "imageUrls": []
                                            }
                                          ],
                                          "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 10
                                          },
                                          "totalPages": 1,
                                          "totalElements": 1
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "검색 키워드 누락 또는 잘못된 정렬 파라미터",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "파라미터 오류", value = """
                                        {
                                          "message": "검색 키워드는 필수입니다."
                                        }
                                    """)
                    })
            )
    })
    @GetMapping
    ResponseEntity<Page<PostDto>> searchPosts(
            @Parameter(description = "검색 키워드", example = "백엔드") @RequestParam String keyword,
            @Parameter(description = "정렬 방식 (newest, likes)", example = "newest") @RequestParam(defaultValue = "newest") SortBy sortBy,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "게시글 작성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "작성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "작성 성공", value = """
                                    {
                                      "id": 1,
                                      "text": "안녕하세요, 첫 게시글입니다!",
                                      "likeCount": 0,
                                      "repostCount": 0,
                                      "user": {
                                        "userId": 1,
                                        "name": "홍길동",
                                        "bio": "백엔드 개발자",
                                        "profileImageUrl": "https://example.com/profile.jpg"
                                      },
                                      "imageUrls": [
                                        "https://quacker-image.s3.ap-northeast-2.amazonaws.com/image1.jpg"
                                      ]
                                    }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "요청 실패", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "본문 누락", value = """
                            {
                              "message": "본문은 공백일 수 없습니다."
                            }
                            """)
            }))
    })
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<PostDto> createPost(@ModelAttribute PostCreateRequestDto request);

    @Operation(
            summary = "게시글 리포스트",
            description = "기존 게시글을 리포스트합니다. 선택적으로 텍스트를 함께 작성할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "리포스트 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "리포스트 성공", value = """
                                        {
                                          "id": 10,
                                          "text": "공유하고 싶은 게시글이에요!",
                                          "likeCount": 0,
                                          "repostCount": 0,
                                          "user": {
                                            "userId": 2,
                                            "name": "홍길동",
                                            "bio": "백엔드 개발자",
                                            "profileImageUrl": "https://example.com/profile.jpg"
                                          },
                                          "originPost": {
                                            "id": 5,
                                            "text": "원본 게시글입니다.",
                                            "likeCount": 15,
                                            "repostCount": 3,
                                            "user": {
                                              "userId": 1,
                                              "name": "원글 작성자",
                                              "bio": "프론트엔드 개발자",
                                              "profileImageUrl": "https://example.com/profile2.jpg"
                                            },
                                            "imageUrls": []
                                          },
                                          "imageUrls": []
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "리포스트 실패", value = """
                                        {
                                          "message": "Post not found"
                                        }
                                    """)
                    })
            )
    })
    @PostMapping
    ResponseEntity<PostDto> repost(
            @Parameter(description = "리포스트할 게시글 ID") @PathVariable Long postId,
            @RequestBody(required = false) PostDto postDto
    );

    @Operation(
            summary = "게시글 수정",
            description = "게시글의 텍스트와 이미지를 수정합니다. 텍스트와 이미지는 선택적으로 수정 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "수정 성공", value = """
                                        {
                                          "id": 1,
                                          "text": "수정된 게시글 내용입니다.",
                                          "likeCount": 5,
                                          "repostCount": 2,
                                          "user": {
                                            "userId": 1,
                                            "name": "홍길동",
                                            "bio": "백엔드 개발자",
                                            "profileImageUrl": "https://example.com/profile.jpg"
                                          },
                                          "imageUrls": [
                                            "https://quacker-image.s3.ap-northeast-2.amazonaws.com/image1.jpg",
                                            "https://quacker-image.s3.ap-northeast-2.amazonaws.com/image2.jpg"
                                          ]
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "게시글 없음", value = """
                                        {
                                          "message": "Post not found"
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "수정 요청 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "잘못된 입력", value = """
                                        {
                                          "message": "이미지는 최대 4개까지 업로드할 수 있습니다."
                                        }
                                    """)
                    })
            )
    })
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<PostDto> updatePost(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Parameter(description = "수정할 게시글 데이터 (본문, 이미지)") @ModelAttribute PostUpdateRequestDto request
    );

    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID를 통해 특정 게시글을 삭제합니다. 삭제 권한은 게시글 작성자에게만 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "게시글 삭제 성공 (내용 없음)"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "삭제 권한 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "권한 없음", value = """
                                        {
                                          "message": "작성자만 게시글을 삭제할 수 있습니다."
                                        }
                                    """)
                    })
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 없음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "게시글 없음", value = """
                                        {
                                          "message": "Post not found"
                                        }
                                    """)
                    })
            )
    })
    @DeleteMapping
    ResponseEntity<Void> deletePost(
            @Parameter(description = "삭제할 게시글 ID") @PathVariable Long postId
    );

}
