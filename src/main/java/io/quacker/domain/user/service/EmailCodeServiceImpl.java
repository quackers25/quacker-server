package io.quacker.domain.user.service;

import io.quacker.common.dao.EmailCodeRepository;
import io.quacker.global.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {

    private final JavaMailSender mailSender;
    private final EmailCodeRepository emailCodeRepository;
    private final Long CODE_EXPIRATION_TIME;

    // 인증 발송 및 캐시추가
    @Override
    public Date sendCode (String email) {
        UUID uuid = UUID.randomUUID();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            log.info(email.trim());
            helper.setTo(email.trim());
            helper.setSubject("Quacker 인증 코드");
            helper.setText(uuid.toString(), false); // HTML false

            helper.setFrom("no-reply@gmail.com", "Quacker"); // 발신자 이름

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new CustomException("발송 실패 : " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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
