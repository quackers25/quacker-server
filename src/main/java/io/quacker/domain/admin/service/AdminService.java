package io.quacker.domain.admin.service;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.admin.dao.AdminRepository;
import io.quacker.domain.admin.dto.AdminCreateDto;
import io.quacker.domain.admin.dto.AdminDto;
import io.quacker.domain.admin.dto.AdminLoginDto;
import io.quacker.domain.admin.entity.Admin;
import io.quacker.domain.auth.dto.JwtTokens;
import io.quacker.domain.auth.service.JwtBlacklistService;
import io.quacker.domain.hashtag.service.HashtagService;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final String CREATE_CODE;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final PostService postService;
    private final HashtagService hashtagService;

    public AdminService(
            AdminRepository adminRepository,
            @Value("${admin.create-code}") String createCode,
            PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, JwtBlacklistService jwtBlacklistService, PostService postService, HashtagService hashtagService
    ) {
        this.adminRepository = adminRepository;
        CREATE_CODE = createCode;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtBlacklistService = jwtBlacklistService;
        this.postService = postService;
        this.hashtagService = hashtagService;
    }

    /**
     * 로그인
     */
    public JwtTokens login(AdminLoginDto adminLoginDto) {

        // 쿠키를 삭제하고 재로그인을 막을 방법이 있나?
        // stateful 밖에 없다.

        String username = adminLoginDto.username();
        String rawPw = adminLoginDto.password();

        // user 엔티티 조회
        var admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("관리자를 찾을 수 없음", HttpStatus.NOT_FOUND.value()));


        String hashedPw = admin.getPassword();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(rawPw, hashedPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }

        // 토큰생성
        String accessToken = jwtTokenUtil.generateAccessToken(admin.getId(), username, "");
        // if (dto.isAutoLogin() ? "30일" : "1~2시간")
        String refreshToken = jwtTokenUtil.generateRefreshToken(admin.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
                .userId(admin.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void join(AdminCreateDto adminCreateDto) {

        String username = adminCreateDto.username();
        String password = adminCreateDto.password();
        String confirmPassword = adminCreateDto.confirmPassword();
        String code = adminCreateDto.code();


        if (adminRepository.existsByUsername(username)) {
            throw new CustomException("사용할 수 없는 유저이름", HttpStatus.BAD_REQUEST.value());
        }

        if (!password.equals(confirmPassword)) {
            throw new CustomException("확인 비밀번호가 불일치", HttpStatus.BAD_REQUEST.value());
        }

        if (!code.equals(CREATE_CODE)) {
            throw new CustomException("생성 코드가 불일치", HttpStatus.BAD_REQUEST.value());
        }

        String hashed = passwordEncoder.encode(password);

        var admin = Admin.builder()
                .username(username)
                .password(hashed)
                .role("ROLE_ADMIN")
                .build();

        adminRepository.save(admin);
    }

    public JwtTokens refresh(String refreshToken) {

        // 리프레시 토큰 유료검사
        if (jwtTokenUtil.isTokenExpired(refreshToken)){
            throw new CustomException("유효하지 않은 토큰", HttpStatus.BAD_REQUEST.value());
        }

        // 블랙리스트 유효 검사
        if (!jwtBlacklistService.validateToken(refreshToken)) {
            throw new CustomException("유효하지 않은 리프레시 토큰", HttpStatus.BAD_REQUEST.value());
        }

        // 리프레시토큰 블랙리스트 등록
        jwtBlacklistService.registerToken(refreshToken);

        // 토큰 subject 추출
        var admin = adminRepository.findById(jwtTokenUtil.extractUserId(refreshToken))
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", HttpStatus.NOT_FOUND.value()));

        // 토큰 재발급
        // TODO 토큰 발급로직 일반화 할 것
        String newAccessToken = jwtTokenUtil.generateAccessToken(admin.getId(), admin.getUsername(), admin.getUsername());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(admin.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String accessToken, String refreshToken) {

        // 익명 사용자 예외처리
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            throw new CustomException("인증되지 않음", 403);
        }

        // 토큰 블랙리스트 등록
        jwtBlacklistService.registerToken(accessToken);
        jwtBlacklistService.registerToken(refreshToken);
    }

    public AdminDto getAdminById(Long id) {
        var admin =adminRepository.findById(id)
                .orElseThrow(() -> new CustomException("관리자가 존재하지 않음", HttpStatus.FORBIDDEN.value()));
        return AdminDto.from(admin);
    }

    public int deletePostByHahstag(String hashtagName) {
        AtomicInteger cnt = new AtomicInteger();
        List<Post> posts = hashtagService.findPostsByHashtag(hashtagName);
        posts.forEach(post -> {
            if (postService.deletePost(post.getId()))
                cnt.getAndIncrement();
        });
        return cnt.get();
    }
}
