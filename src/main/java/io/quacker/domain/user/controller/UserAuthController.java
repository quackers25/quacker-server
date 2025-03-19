package io.quacker.domain.user.controller;

import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.dto.UserLoginDto;
import io.quacker.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserService userService;

    /**
     * 로그인 엔드포인트
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }
//        userService.login(userCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(userLoginDto));
    }

    /**
     * 가입 엔드포인트
     * @param userCreateDto
     * @return JWT 토큰 반환
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(userCreateDto));
    }
}
