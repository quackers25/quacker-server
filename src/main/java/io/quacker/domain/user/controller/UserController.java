package io.quacker.domain.user.controller;

import io.quacker.domain.user.controller.api.UserApi;
import io.quacker.domain.user.dto.UserUpdateDto;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@EnableMethodSecurity
@RequestMapping("/api/v1/users")
public class UserController implements UserApi {

    private final UserService userService;

    //비공개 토글
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #userId == principal.userId")
    @PatchMapping("/{userId}/visibility")
    public ResponseEntity<?> toggleVisibility(@PathVariable("userId") Long userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("isPrivate", userService.toggleVisibility()));
    }

    //삭제 "요청"
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #userId == principal.userId")
    @PostMapping("/{userId}/delete")
    public ResponseEntity<?> requestDelete(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.requestDeletion());
    }

    //삭제 취소
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #userId == principal.userId")
    @PostMapping("/{userId}/abort")
    public ResponseEntity<?> abort(@PathVariable("userId") Long userId) {
        userService.abortUserDeletion();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("result", true));
    }

    // 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }

    // 프로필 수정
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> editProfile(
            @PathVariable("userId") Long userId,
            @RequestBody UserUpdateDto userUpdateDto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateProfile(userId, userUpdateDto));
    }
}
