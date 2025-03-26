package io.quacker.domain.userfollowing.api;

import io.quacker.domain.userfollowing.dto.FollowRequestDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Following", description = "유저 팔로잉 관련 API")
public interface UserFollowApi {

    @ApiResponses({
        @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "유저 팔로잉 성공", value = """
                {
                    "userId": 1,
                    "name": "이름",
                    "bio": "bio",
                    "profileImageUrl": "https://example.com/abc.jpg"
                }
                """),})),
        @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "팔로잉 실패 - 자기 자신 팔로잉", value = """
                {
                    "message": "자기 자신을 팔로잉 할 수 없습니다."
                }
                """),})),
        @ApiResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "팔로잉 실패 - 이미 등록한 팔로잉", value = """
                {
                    "message": "이미 팔로잉 한 유저 입니다."
                }
                """)}))})
    ResponseEntity<?> addFollowing(@RequestBody FollowRequestDto followRequestDto);

    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "유저 언 팔로잉 성공", value = """
                {
                    "userId": 1,
                    "name": "이름",
                    "bio": "bio",
                    "profileImageUrl": "https://example.com/abc.jpg"
                }
                """),})),
        @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "언 팔로잉 실패 - 팔로잉 하지 않은 유저 언팔로잉", value = """
                {
                    "message": "팔로잉 정보가 없습니다."
                }
                """),})),})
    @DeleteMapping
    ResponseEntity<?> unFollowing(@PathVariable Long userId);

    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "팔로잉 유저 목록", value = """
                [
                    {
                        "userId": 1,
                        "name": "이름",
                        "bio": "bio",
                        "profileImageUrl": "https://example.com/abc.jpg"
                    },
                    {
                        "userId": 2,
                        "name": "이름2",
                        "bio": "bio2",
                        "profileImageUrl": "https://example.com/bca.jpg"
                    },
                ]
                """),})),})
    @GetMapping
    ResponseEntity<?> getAllFollowingByUserId(@PathVariable Long userId);

    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "팔로워 유저 목록", value = """
                [
                    {
                        "userId": 4,
                        "name": "이름3",
                        "bio": "bio",
                        "profileImageUrl": "https://example.com/abc.jpg"
                    },
                    {
                        "userId": 5,
                        "name": "이름4",
                        "bio": "bio2",
                        "profileImageUrl": "https://example.com/bca.jpg"
                    },
                ]
                """),})),})
    @GetMapping
    ResponseEntity<?> getAllFollowerByUserId(@PathVariable Long userId);

}
