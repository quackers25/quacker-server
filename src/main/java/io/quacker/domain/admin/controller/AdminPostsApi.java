package io.quacker.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminPostsApi {

    ResponseEntity<?> deleteAllHashtaggedPost(
            @RequestParam("hasHashtag") String hashtagName
    );

}
