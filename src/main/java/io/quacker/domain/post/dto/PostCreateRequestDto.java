package io.quacker.domain.post.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostCreateRequestDto(
        String text,
        List<MultipartFile> images
) {}