package io.quacker.domain.postmention.controller;

import io.quacker.domain.postmention.service.PostMentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class PostMentionController {

    private final PostMentionService postMentionService;

    @GetMapping("users/{userId}/mentions")
    public ResponseEntity<?> getPostByMentionUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postMentionService.getAllMentionPostByUser(userId));
    }
}
