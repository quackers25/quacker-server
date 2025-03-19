package io.quacker.domain.user.service;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.user.dao.UserRepositoy;
import io.quacker.domain.user.dto.*;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepositoy userRepositoy;

    /**
     * REQ_002	회원가입
     * 생성 성공 시에 생성된 userDto 반환
     * @param dto, UserCreadentialDto
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
            throw new CustomException("Invalid password", 500);
        }
        if (userRepositoy.existsByEmail(email)) {
            throw new CustomException("Email exists", 500);
        }

        User user = User.fromCreateDtoWithHashedPassword(dto, passwordEncoder.encode(rawPw));

        try {
            User result = userRepositoy.save(user);
            return UserDto.from(result);
        } catch (Exception e){
            throw new CustomException("Failed to save user", 500);
        }
    }

    /**
     * REQ_001	로그인
     * 로그인 성공시 정보를 반환.
     * @return String, 토큰 발급
     */
    public String login(UserLoginDto dto) {

        String email = dto.email();
        String rawPw = dto.password();

        User user = userRepositoy.findByEmail(email)
                .orElseThrow(() -> new CustomException("", 500));

        String hashedPw = user.getPassword();

        if (!passwordEncoder.matches(rawPw, hashedPw)) {
            throw new CustomException("Invalid password", 500); //ToDo: 로그인 실패 객체 정의할 것
        }

        return jwtTokenUtil.generateToken(user.getId(), user.getEmail(), user.getName());
    }

    /**
     * REQ_003	회원 탈퇴 요청
     */
    public void requestWithdraw() {}

    /**
     * REQ_013	공개여부 토글
     */
    public void setPublic() {}

    /**
     * REQ_004	회원 복구
     */
    public void restoreUser() {}

    /**
     * REQ_005	비밀번호 재설정
     */
    public void resetPassword() {

    }

    /**
     * REQ_006	아이디 찾기
     * 이메일 찾기
     * 가입한 이메일로 메시지 발솔
     */
    public void findEmail() {}

    /**
     * REQ_007	아이디 중복 확인 기능
     */
    public boolean checkDuplicateEmail() {return false;}

    /**
     * REQ_008	이메일 중복 확인 기능
     */
    public boolean checkDuplicateUsername() {return false;}

    /**
     * REQ_011	프로필 조회
     */
    public UserDto getUserById(Long userId) {
        return UserDto.from(userRepositoy.findById(userId)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없음", 404)));
    }

    /**
     * REQ_012	프로필 수정
     */
    @Transactional
    public UserDto updateUserProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            UserUpdateDto dto
    ) {
        User user = customUserDetails.getUser();
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
   @Transactional
   public void toggleVisibility(
           CustomUserDetails customUserDetails
   ) {
       User user = customUserDetails.getUser();
       user.updateVisibility(!user.isPrivate());
   }
}
















