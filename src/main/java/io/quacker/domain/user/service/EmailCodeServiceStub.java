package io.quacker.domain.user.service;

import io.quacker.common.dao.EmailCodeRepository;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EmailCodeServiceStub implements EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;
    private final Long CODE_EXPIRATION_TIME;

    @Override
    public Date sendCode (String email) {
        UUID uuid = UUID.randomUUID();
        log.info(email + " CODE : "+ uuid.toString());

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + CODE_EXPIRATION_TIME);

        //Redis에 추가
        emailCodeRepository.setex(email, exp, uuid.toString());

        return now;
    }

    @Override
    public boolean verifyCode(String email, String code) {

        // 이메일에 해당하는 검증이 있는지 확인
        var result = emailCodeRepository.get(email)
                .orElseThrow(()->new CustomException("만료되거나 존재하지않은 인증", HttpStatus.BAD_REQUEST.value()));

        // 코드 일치 여부 반환
        boolean verified = result.equals(code);

        // 인증 성공시 인증캐시 삭제
        if (verified) {
            emailCodeRepository.delete(email);
        }
        return verified;
    }

    @Override
    public void deleteCodeByEmail(String email) {
        emailCodeRepository.delete(email);
    }
}
