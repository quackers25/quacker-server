package io.quacker.domain.user.controller;

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
public class UserController {

    private final UserService userService;

//    @PreAuthorize("hasAuthority('ROLE_ADMIN_READ') or #userId == principal.userId")
    @PatchMapping("/visibility")
    public ResponseEntity<?> toggleVisibility(){
        userService.toggleVisibility();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("result", true));
    }

    //삭제 "요청"
//    @PreAuthorize("hasAuthority('ROLE_ADMIN_READ') or #userId == principal.userId")
    @PostMapping("/delete")
    public ResponseEntity<?> requestDelete() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.requestDeletion());
    }

    //삭제 취소
//    @PreAuthorize("hasAuthority('ROLE_ADMIN_READ') or #userId == principal.userId")
    @PostMapping("/abort")
    public ResponseEntity<?> abort() {
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN_READ') or #userId == principal.userId")
    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> editProfile(
            @PathVariable("userId") Long userId,
            @RequestBody UserUpdateDto userUpdateDto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateMyProfile(userId, userUpdateDto));
    }
}
