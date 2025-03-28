package io.quacker.domain.user.service;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.auth.dto.JwtTokens;
import io.quacker.domain.auth.service.JwtBlacklistService;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.dto.*;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final JwtBlacklistService jwtBlacklistService;
    private final UserDeletionService userDeletionService;
    private final EmailCodeService emailCodeService;
    private final Long PENDDING_TIME = 60000L;
    /**
     * 토큰 재발급 요청
     * @param refreshToken
     */
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
        User user = userRepository.findById(jwtTokenUtil.extractUserId(refreshToken))
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", HttpStatus.NOT_FOUND.value()));

        // 토큰 재발급
        String newAccessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getEmail(), user.getName());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    /**
     * REQ_002	회원가입
     * 생성 성공 시에 생성된 userDto 반환
     * @param dto, UserCreateDto
     * @Return UserDto
     */
    public UserDto join(UserCreateDto dto) {

        /**
         * 인증받지않은 유저
         */

        String email = dto.email();
        String rawPw = dto.password();
        String rawConfirmPw = dto.confirmPassword();

        // 암호와 암호확인문 일치 확인
        if (!rawPw.equals(rawConfirmPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("사용할 수 없는 이메일", 409);
        }

        // 엔티티 생성
        User user = User.fromCreateDtoWithHashedPassword(dto, passwordEncoder.encode(rawPw));
        return UserDto.from(userRepository.save(user));
    }


    /**
     * REQ_001	<h4>로그인</h4>
     * 로그인 성공시 정보를 반환.
     * @param dto 로그인 정보 dto
     * @return JWT토큰 dto
     */
    public JwtTokens login(UserLoginDto dto) {

        // 쿠키를 삭제하고 재로그인을 막을 방법이 있나?
        // stateful 밖에 없다.

        String email = dto.email();
        String rawPw = dto.password();

        // user 엔티티 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", HttpStatus.NOT_FOUND.value()));

        if (user.getDeletedAt() != null) {
            // 삭제된 유저
            throw new CustomException("유저를 찾을 수 없음", HttpStatus.NOT_FOUND.value());
        }

        String hashedPw = user.getPassword();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(rawPw, hashedPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }

        // 토큰생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), email, "user");
        // if (dto.isAutoLogin() ? "30일" : "1~2시간")
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * REQ_
     * <h4>로그아웃</h4>
     * 소지 토큰 만료시 로그아웃으로 간주됩니다.
     * @param accessTokenId
     * @param refreshTokenId
     */
    public void logout(String accessTokenId, String refreshTokenId) {
//
//        // 익명 사용자 예외처리
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
//            throw new CustomException("인증되지 않음", 403);
//        }

        // 토큰 블랙리스트 등록
        // 블랙리스트에 기존 토큰 등록(만료시킴)
        jwtBlacklistService.registerToken(accessTokenId);
        jwtBlacklistService.registerToken(refreshTokenId);
    }


    /**
     * REQ_003	회원 탈퇴 요청
     * @return 탈퇴요청시간, 탈퇴예정시간
     */
    public Map requestDeletion() {

        User user = getCurrentUser();

        if (user.isLocked()) {
            throw new CustomException("이미 탈퇴 요청함", HttpStatus.BAD_REQUEST.value());
        }
        // User 조회
        Date exp = new Date(System.currentTimeMillis() + PENDDING_TIME);
        Date current = new Date();
        userDeletionService.addRequest(user.getId(), exp);


        // User 정지
        user.freeze();

        return Map.of("requestAt", current, "deleteAt", exp);
    }

    /**
     * REQ_004	<h4>회원 복구</h4>
     */
    public void abortUserDeletion() {
        /*
            테이블에서 삭제, 삭제될때 요청이 들오올수도
            throw new CustomException("존재하지 않는 유저", 403);
         */

        User user = getCurrentUser();
        user.unfreeze();
        userDeletionService.abortRequest(String.valueOf(user.getId()));
    }

    /**
     * REQ_005	<h4>비밀번호 재설정</h4>
     * 비밀번호, 확인 비밀번호를 확인 후 저장합니다.
     *
     * 비밀번호 변경 세션을 만들어서 인증을 받아야함
     *
     *
     * @param dto 비밀번호, 확인 비밀번호를 담은 dto
     */
    @Transactional
    public void resetPassword(UserResetPasswordDto dto) {

        // 인증코드 검증
        if(!emailCodeService.verifyCode(dto.email(), dto.code())) {
            throw new CustomException("인증코드가 유효하지않음", HttpStatus.BAD_REQUEST.value());
        }

        // 새 비밀번호가 유효하지않음
        if (!dto.newPassword().equals(dto.newConfirmPassword()))
            throw new CustomException("비밀번호가 일치하지 않음", 400);

        // 이메일로 유저 찾기
        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));

        // 인코딩 후 저장
        String hashedPw = passwordEncoder.encode(dto.newPassword());
        user.changePassword(hashedPw);
    }

    /**
     * REQ_006	아이디 찾기 <br/>
     * 다른 정보로 계정 찾아 이메일 찾기, 마스킹된 이메일 얻기, 우선 힌트로 제공
     * @param hint 사용자가 입력한 힌트
     * @return 마스킹된 이메일
     */
    public String getEmailByHint(String hint) {
        User user = userRepository.findByHint(hint)
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));

        String email = user.getEmail();
        String[] part = email.split("@");
        StringBuilder sb = new StringBuilder();
        sb.append(part[0].substring(0, 4)).append("****@").append(part[1]);
        return sb.toString();
    }

    /**
     * REQ_007	이름 중복 확인 기능
     */
    public boolean checkDuplicateUsername(String name) {
        return userRepository.existsByName(name);
    }

    /**
     * REQ_008	이메일 중복 확인 기능
     * @param email
     * @return boolean
     */
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * REQ_011	프로필 조회
     * 특정 공개 사용자 조회,
     * @param userId, Long
     * @return UserDto
     */
    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", HttpStatus.BAD_REQUEST.value()));

        // 본인이 아니고 비공개라면
        if (user.isPrivate() && !userId.equals(user.getId()))
            throw new CustomException("비공개 유저", HttpStatus.FORBIDDEN.value());

        return UserDto.from(user);
    }

    /**
     * REQ_012	프로필 수정
     * 특정 프로필 수정
     * @param userId, Long
     * @param dto, UserUpdateDto
     * @return UserDto
     */
    public UserDto updateProfile(Long userId, UserUpdateDto dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));
        user.updateProfile(
                dto.name(),
                dto.bio(),
                dto.avatarImageUrl(),
                dto.isLocked(),
                dto.isPrivate()
        );

        return UserDto.from(user);
    }

    /**
     * REQ_013	공개여부 토글
     */
    public void toggleVisibility() {
        var user = getCurrentUser();
        user.updateVisibility(!user.isPrivate());
    }

    /**
     *
     * @return
     */
    public Map<?,?> sendCode(String email) {
        if(!userRepository.existsByEmail(email))
            throw new CustomException("유저를 찾을 수 없음", 404);
        return Map.of("sentAT", emailCodeService.sendCode(email));
    }

    /**
     *
     * @param email
     * @return
     */
    @Transactional
    public boolean verifyEmailCode(String email, String code) {
        User user = getCurrentUser();

        // 이메일 보내기
        // 언제 이메일을 보냈는가를 확인해야함, 횟수 제한을 위하여
        if (!emailCodeService.verifyCode(email, code)) {
            throw new CustomException("코드 인증 실패", HttpStatus.BAD_REQUEST.value());
        }

        user.setVerified(true);
        //ROLE 변경? 불필요 어차피 이 요청이 끝나고 다시 인증객체가 생성되므로

        return true;
    }

    /**
     * 현재 로그인된 User 영속성 엔티티 반환.
     * @return User
     */
    public User getCurrentUser() throws CustomException{

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            throw new CustomException("인증되지 않음", HttpStatus.BAD_REQUEST.value());
        }

        if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new CustomException("인증되지 않음", 500);
        }

        Long userId = ((CustomUserDetails)auth.getPrincipal()).getUserId();

        return userRepository.findById(userId)
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));
    }
}

















