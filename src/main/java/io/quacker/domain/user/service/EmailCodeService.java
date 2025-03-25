package io.quacker.domain.user.service;

import java.util.Date;

public interface EmailCodeService {

    Date sendCode(String email);

    boolean verifyCode(String email, String code);

    void deleteCodeByEmail(String email);
}
