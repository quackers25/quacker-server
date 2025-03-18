package io.quacker.domain.user.service;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.user.dao.UserRepositoy;
import io.quacker.domain.user.dto.UserLoginDto;
import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.dto.UserDto;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
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
            throw new CustomException("Invalid password", 500); //ToDo: 로그인 실패 객체 정의할 것
        }
        if (!userRepositoy.existsByEmail(email)) {
            throw new CustomException("Email exists", 500); //ToDo: 로그인 실패 객체 정의할 것
        }


        User user = dto.toUserWtihHashedPassword(passwordEncoder.encode(rawPw));

        try {
            User result = userRepositoy.save(user);
            return UserDto.from(result);
        } catch (Exception e){
            throw new CustomException("Failed to save user", 500); //ToDo: 저장 실패 객체 정의할 것
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
    public void withDraw() {}

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
     */
    public void findUser() {

    }

    /*
    REQ_007	아이디 중복 확인 기능
    REQ_008	이메일 중복 확인 기능
    REQ_009	로그아웃
    REQ_010	멘션
    REQ_011	프로필 조회
    REQ_012	프로필 수정
    REQ_013	공개여부 토글

     */
}
