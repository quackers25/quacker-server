package io.quacker.domain.hashtag.controller;

import io.quacker.domain.hashtag.api.HashtagApi;
import io.quacker.domain.hashtag.dto.HashtagResponse;
import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HashtagController implements HashtagApi {

    private final HashtagService hashtagService;

    @Override
    public ResponseEntity<Page<HashtagResponse>> getTrendingHashtags(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("postCount").descending());
        Page<HashtagResponse> hashtags = hashtagService.getTrendingHashtags(pageRequest);
        return ResponseEntity.ok(hashtags);
    }

    @Override
    public ResponseEntity<Page<HashtagResponse>> searchHashtags(String query, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("postCount").descending());
        Page<HashtagResponse> hashtags = hashtagService.searchHashtags(query, pageRequest);
        return ResponseEntity.ok(hashtags);
    }
} 