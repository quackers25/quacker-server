package io.quacker.domain.user.service;

import io.quacker.common.dao.EmailCodeRepository;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EmailServiceStub implements EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;

    @Override
    public Date sendCode (String to) {
        UUID uuid = UUID.randomUUID();
        log.info(uuid.toString());
        return new Date();
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
