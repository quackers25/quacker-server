package io.quacker.domain.user.service;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.dto.UserDto;
import io.quacker.domain.user.dto.UserLoginDto;
import io.quacker.domain.user.dto.UserUpdateDto;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

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
         */

        if (!rawPw.equals(rawConfirmPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("사용할 수 없는 이메일", 409);
        }

        User user = User.fromCreateDtoWithHashedPassword(dto, passwordEncoder.encode(rawPw));

        User result = userRepository.save(user);
        return UserDto.from(result);
    }

    /**
     * REQ_001	로그인
     * 로그인 성공시 정보를 반환.
     * @param dto, UserLoginDto
     * @return String, JWT토큰
     */
    public String login(UserLoginDto dto) {
        String email = dto.email();
        String rawPw = dto.password();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", 404));

        String hashedPw = user.getPassword();
        if (!passwordEncoder.matches(rawPw, hashedPw)) {
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        }

        return jwtTokenUtil.generateToken(user.getId(), user.getEmail(), user.getName());
    }

    /**
     * REQ_003	회원 탈퇴 요청
     */
    public LocalDateTime requestWithdraw() {
        var user = getCurrentUser();
        user.freeze();
        if (user.isLocked())
            throw new CustomException("이미 탈퇴 요청함", 400);
        /*
            일정기간 유예 후 삭제할 방법 생략
            별도의 테이블 필요할 듯
         */

        return LocalDateTime.now();
    }

    /**
     * REQ_004	회원 복구
     */
    public void restoreUser() {
        /*
            테이블에서 삭제, 삭제될때 요청이 들오올수도
            동시성 문제? 성공 여부판단 필요
            throw new CustomException("존재하지 않는 유저", 403);
         */
    }

    /**
     * REQ_005	비밀번호 재설정
     */
    public void resetPassword(UserCreateDto dto) {
        if (!dto.password().equals(dto.confirmPassword()))
            throw new CustomException("비밀번호가 일치하지 않음", 400);
        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));

        String hashedPw = passwordEncoder.encode(dto.password());
        user.changePassword(hashedPw);
    }

    /**
     * REQ_006	아이디 찾기
     * 다른 정보로 계정 찾아 이메일 찾기, 마스킹된 이메일 얻기
     */
    public String getEmailHint() {
        return "";
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
//    public UserDto updateUserProfile(Long userId, UserUpdateDto dto) {
//        var user = userRepository.findById(userId)
//                dto.name(),
//                dto.bio(),
//                dto.avatarImageUrl(),
//                dto.isLocked(),
//                dto.isPrivate()
//        );
//
//        return UserDto.from(user);
//    }

    /**
     * REQ_013	공개여부 토글
     */
    public void toggleVisibility() {
        var user = getCurrentUser();
        user.updateVisibility(!user.isPrivate());
    }

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
       if (auth == null) {
           throw new CustomException("인증되지 않음", 403);
       }

       if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
           throw new CustomException("인증되지 않음", 500);
       }

       Long userId = ((CustomUserDetails)auth.getPrincipal()).getUserId();

       return userRepository.findById(userId)
               .orElseThrow(()-> new CustomException("유저를 찾을 수 없음", 404));
   }
}

















