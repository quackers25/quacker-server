package io.quacker.domain.admin.controller;

import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    // 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }

    // 유저 강제삭제 처리
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
//        return userService.del(userId);
        return null;
    }

    // 계정 활성화 설정
    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<?> suspend (@PathVariable("userId") Long userId) {
        return null;
    }
}
