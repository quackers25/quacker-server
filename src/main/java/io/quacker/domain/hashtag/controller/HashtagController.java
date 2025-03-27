package io.quacker.domain.hashtag.controller;

import io.quacker.domain.hashtag.dto.HashtagResponse;
import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/trending")
    public ResponseEntity<Page<HashtagResponse>> getTrendingHashtags(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Hashtag> hashtags = hashtagService.getTrendingHashtags(pageable);
        return ResponseEntity.ok(hashtags.map(HashtagResponse::from));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HashtagResponse>> searchHashtags(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Hashtag> hashtags = hashtagService.searchHashtags(query, pageable);
        return ResponseEntity.ok(hashtags.map(HashtagResponse::from));
    }
} 