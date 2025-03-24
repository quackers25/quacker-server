package io.quacker.domain.userfollowing.controller;

import io.quacker.domain.userfollowing.dto.FollowRequestDto;
import io.quacker.domain.userfollowing.service.UserFollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserFollowingController {

    private final UserFollowingService userFollowingService;

    @PostMapping("followings")
    public ResponseEntity<?> addFollowing(@RequestBody FollowRequestDto followRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userFollowingService.followingUser(followRequestDto));
    }

    @GetMapping("followings/{userId}")
    public ResponseEntity<?> getAllFollowingByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userFollowingService.getAllFollowingUserId(userId));
    }

    @GetMapping("followers/{userId}")
    public ResponseEntity<?> getAllFollowerByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userFollowingService.getAllFollowerByUserId(userId));
    }

    @DeleteMapping("followings/{userId}")
    public ResponseEntity<?> unFollowing(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userFollowingService.unfollowingUser(userId));
    }

}
