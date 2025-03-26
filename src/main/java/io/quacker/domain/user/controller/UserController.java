package io.quacker.domain.user.controller;

import io.quacker.domain.user.dto.UserUpdateDto;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/visibility")
    public ResponseEntity<?> toggleVisibility(){
        userService.toggleVisibility();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("result", true));
    }

    //삭제 "요청"
    @PostMapping("/delete")
    public ResponseEntity<?> requestDelete() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.requestDeletion());
    }

    //삭제 취소
    @PostMapping("/abort")
    public ResponseEntity<?> abort() {
        userService.abortUserDeletion();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("result", true));
    }

    // 프로필
    @GetMapping("/")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserProfile());
    }

    // 프로필 수정
    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(@RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUserProfile(userUpdateDto));
    }
}
