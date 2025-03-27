package io.quacker.domain.hashtag.api;

import io.quacker.domain.hashtag.dto.HashtagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "해시태그 API", description = "해시태그를 조회하고 관리하는 API입니다.")
@RequestMapping("/api/v1/hashtags")
public interface HashtagApi {

    @Operation(
            summary = "해시태그 검색",
            description = "입력된 키워드로 해시태그를 검색합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "검색 결과", value = """
                            {
                              "content": [
                                {
                                  "id": 1,
                                  "name": "스프링",
                                  "postCount": 5
                                },
                                {
                                  "id": 2,
                                  "name": "스프링부트",
                                  "postCount": 3
                                }
                              ],
                              "totalElements": 2,
                              "totalPages": 1,
                              "size": 10,
                              "number": 0
                            }
                            """)
            }))
    })
    @GetMapping("/search")
    ResponseEntity<Page<HashtagResponse>> searchHashtags(
            @Parameter(description = "검색할 키워드", example = "스프링") @RequestParam String query,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    );

    @Operation(
            summary = "인기 해시태그 조회",
            description = "가장 많이 사용된 해시태그를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "인기 해시태그 목록", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "인기 해시태그", value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "name": "스프링",
                          "postCount": 10
                        },
                        {
                          "id": 2,
                          "name": "자바",
                          "postCount": 8
                        }
                      ],
                      "totalElements": 2,
                      "totalPages": 1,
                      "size": 10,
                      "number": 0
                    }
                    """)
    }))
    @GetMapping("/trending")
    ResponseEntity<Page<HashtagResponse>> getTrendingHashtags(
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    );
} 