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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final Long PENDDING_TIME = 60000L;
    /**
     * 토큰 재발급 요청
     * @param token
     */
    public JwtTokens refresh(String token) {

        // 리프레시 토큰 유료검사
        if (jwtTokenUtil.isTokenExpired(token)){
            throw new CustomException("유효하지 않은 토큰", HttpStatus.BAD_REQUEST.value());
        }

        // 블랙리스트 유효 검사
        if (!jwtBlacklistService.validateToken(token)) {
            throw new CustomException("유효하지 않은 리프레시 토큰", HttpStatus.BAD_REQUEST.value());
        }

        // 리프레시토큰 블랙리스트 등록
        jwtBlacklistService.registerToken(token);

        // 토큰 subject 추출
        User user = userRepository.findById(jwtTokenUtil.extractUserId(token))
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", HttpStatus.NOT_FOUND.value()));

        // 토큰 재발급
        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getEmail(), user.getName());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * REQ_002	회원가입
     * 생성 성공 시에 생성된 userDto 반환
     * @param dto, UserCreateDto
     * @Return UserDto
     */
    public UserDto join(UserCreateDto dto) {

        String email = dto.email();
        String rawPw = dto.password();
        String rawConfirmPw = dto.confirmPassword();

        /*
         *  이메일 유효 검사 추가 할 것
         *  인증 세션 확인
         */

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

        String hashedPw = user.getPassword();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(rawPw, hashedPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }

        // 토큰생성
        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), email, "");
        // if (dto.isAutoLogin() ? "30일" : "1~2시간")
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId());

        // 토큰 Dto 반환
        return JwtTokens.builder()
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

        // 익명 사용자 예외처리
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            throw new CustomException("인증되지 않음", 403);
        }

        // 토큰 블랙리스트 등록
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

        // User 정지
        user.freeze();

        Date exp = new Date(System.currentTimeMillis() + PENDDING_TIME);
        Date current = new Date();

        // User 삭제 대기
        userDeletionService.addRequest(user.getId(), exp);

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
    public void resetPassword(UserCreateDto dto) {

        //Todo : 인증 세션, 또는 인증코드리스트 유지 존재하는 요청인지 확인해한다.

        if (!dto.password().equals(dto.confirmPassword()))
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));

        String hashedPw = passwordEncoder.encode(dto.password());
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
     * 나의 프로필 조회
     * @return UserDto
     */
    public UserDto getUserProfile() {
        return UserDto.from(getCurrentUser());
    }

    /**a
     * REQ_011	프로필 조회
     * 특정 사용자 조회
     * @param userId, Long
     * @return UserDto
     */
    public UserDto getUserById(Long userId) {
        return UserDto.from(userRepository.findById(userId)
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404)));
    }

    /**
     * REQ_012	프로필 수정
     * 나의 프로필 수정
     * @param dto, UserUpdateDto
     * @return UserDto
     */
    public UserDto updateUserProfile(UserUpdateDto dto) {
        var user = getCurrentUser();
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
     * REQ_012	프로필 수정
     * 특정 프로필 수정
     * @param userId, Long
     * @param dto, UserUpdateDto
     * @return UserDto
     */
    public UserDto updateUserProfile(Long userId, UserUpdateDto dto) {
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
     * @param email
     * @return
     */
    public boolean verifyEmail(String email) {
        // 이메일 보내기
        // 언제 이메일을 보냈는가를 확인해야함, 횟수 제한을 위하여


        LocalDateTime expiration = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
        if (expiration.isAfter(LocalDateTime.now())) {

        }
        return false;
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

















