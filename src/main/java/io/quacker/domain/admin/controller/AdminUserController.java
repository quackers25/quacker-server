package io.quacker.domain.admin.controller;

import io.quacker.domain.admin.controller.api.AdminUserApi;
import io.quacker.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/v1/admins/users")
@RequiredArgsConstructor
public class AdminUserController implements AdminUserApi {

    private final UserService userService;

    // 유저 강제삭제 처리
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteById(userId));
    }
}
