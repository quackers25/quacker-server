package io.quacker.domain.admin.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "관리자 유저 관리 api", description = "어드민 API")
public interface AdminUserApi {

    @Operation(summary = "유저 완전 삭제", description = "유저를 데이터베이스에서 완전히 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제한 개수를 반환합니다", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "삭제 성공", value = """
                            
                            """)
            })
            )
    })
    ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId);
}
