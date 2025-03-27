package io.quacker.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateRequestDto(
        @Schema(description = "수정할 게시글 텍스트", example = "내용을 조금 고쳤습니다.")
        String text,

        @Schema(description = "새로 추가할 이미지 파일 목록", type = "array", implementation = MultipartFile.class)
        List<MultipartFile> newImages,

        @Schema(description = "삭제할 이미지 ID 목록", example = "[101, 102]")
        List<Long> deleteImageIds
) {}