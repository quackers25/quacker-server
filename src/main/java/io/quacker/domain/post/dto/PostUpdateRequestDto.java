package io.quacker.domain.post.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateRequestDto(
        String text,
        List<MultipartFile> newImages,
        List<Long> deleteImageIds
) {}