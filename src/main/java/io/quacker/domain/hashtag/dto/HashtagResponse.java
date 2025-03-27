package io.quacker.domain.hashtag.dto;

import io.quacker.domain.hashtag.entity.Hashtag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HashtagResponse {
    private Long id;
    private String name;
    private int postCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HashtagResponse from(Hashtag hashtag) {
        return HashtagResponse.builder()
                .id(hashtag.getId())
                .name(hashtag.getName())
                .postCount(hashtag.getPostCount())
                .createdAt(hashtag.getCreatedAt())
                .updatedAt(hashtag.getUpdatedAt())
                .build();
    }
} 