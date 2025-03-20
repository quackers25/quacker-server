package io.quacker.domain.user.service;

import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import io.quacker.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", 404));

        return new CustomUserDetails(user);
    }
}
