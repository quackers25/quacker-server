package io.quacker.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostCreateRequestDto(
        @Schema(description = "게시글 본문", example = "안녕하세요, 첫 글입니다.")
        String text,

        @Schema(description = "업로드할 이미지 파일 목록 (최대 4개)", type = "array", implementation = MultipartFile.class)
        List<MultipartFile> images
) {}