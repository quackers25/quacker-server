package io.quacker.domain.user.service;

import io.quacker.domain.user.dao.UserRepositoy;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoy userRepositoy;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoy.findByEmail(email)
                .orElseThrow(()-> new CustomException("Invaild", 500));

        return new CustomUserDetails(user);
    }
}
