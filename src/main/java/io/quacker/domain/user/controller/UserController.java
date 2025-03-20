package io.quacker.domain.user.controller;

import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/visibility")
    public ResponseEntity<?> toggleVisibility(){
        userService.toggleVisibility();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", true));
    }
}
