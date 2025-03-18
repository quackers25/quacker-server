package io.quacker.domain.user.controller;

import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.dto.UserLoginDto;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserService userService;

    /**
     * 로그인 엔드포인트
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
//        userService.login(userCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(userLoginDto));
    }

    /**
     * 가입 엔드포인트
     * @param userCreateDto
     * @return JWT 토큰 반환
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(userCreateDto));
    }
}
