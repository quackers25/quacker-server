package io.quacker.global.config;

import io.quacker.common.dao.EmailCodeRepository;
import io.quacker.domain.user.service.EmailCodeService;
import io.quacker.domain.user.service.EmailCodeServiceImpl;
import io.quacker.domain.user.service.EmailServiceStub;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final JavaMailSender mailSender;
    private final EmailCodeRepository emailCodeRepository;

//    @Bean
//    public EmailCodeService emailService() {
//        return new EmailServiceStub();
//    }

    @Bean
    public EmailCodeService emailCodeService(
            JavaMailSender mailSender,
            EmailCodeRepository emailCodeRepository,
            @Value("${email-code.expiration}") Long CODE_EXPIRATION_TIME
    ) {
        return new EmailCodeServiceImpl(mailSender, emailCodeRepository, CODE_EXPIRATION_TIME);
    }
}
