package io.quacker.domain.user.controller;

import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", true));
    }

    //삭제 "요청"
    @PostMapping("/delete")
    public ResponseEntity<?> requestDelete(@PathVariable("userId") Long userId) {
        //Todo : RESTful 한 설계이어야하나? 리소스 식별자가 필요한가.
        return ResponseEntity.status(HttpStatus.OK).body(userService.requestDeletion());
    }

    //삭제 취소
    @PostMapping("/abort")
    public ResponseEntity<?> abort(@PathVariable("userId") Long userId) {
        //Todo : RESTful 한 설계이어야하나?
        userService.abortUserDeletion();
        return ResponseEntity.status(HttpStatus.OK).body("done");
    }

    //힌트로 이메일 찾기
    @GetMapping("/hint")
    public ResponseEntity<?> getEmailhint(@RequestBody String hint) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getEmailByHint(hint));
    }

    // 이메일 중복 확인?
    @GetMapping("/email/{email}")
    public ResponseEntity<?> duplicateEmail(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkDuplicateEmail(email));
    }

    // 사용자 이름 중복 확인
    @GetMapping("/username/{username}")
    public ResponseEntity<?> duplicateUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkDuplicateUsername(username));
    }

    // 인증 세션 생성
    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody String email) {
        // 1. 이메일 중복확인
        // 2. 이메일기반으로 세션 생성(유효 15분)
        // 3. 이메일로 인증하고 인증성공시 유효한 세선 전환
        //ToDo 생성된 세션의 식별자(세션id)와 생성시간 만료시간
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    // 사용자 비밀번호 변경(인증 세션 필요)
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserCreateDto userCreateDto) {
        // 1. 세션 인증 확인
        // 2. 세션종료, 세션 삭제
        userService.resetPassword(userCreateDto);
        //TODO : 성공 response dto?
        return ResponseEntity.status(HttpStatus.OK).body("done");
    }

    @PutMapping("/session/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody UserCreateDto userCreateDto) {
        // 1. 생성시간이 만료된 세션을 삭제
        // 2. 인증 성공시 세션 상태를 verify로 변경
        // TODO : join api에 verify 하고 성공시에 user.verified =true로 변경하여 save할 것
        return null;
    }
}
