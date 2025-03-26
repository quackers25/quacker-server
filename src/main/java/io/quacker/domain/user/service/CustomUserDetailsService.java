package io.quacker.domain.user.service;

import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException("UsernameNotFoundException", 404));

        if (user.getDeletedAt() != null) {
            throw new CustomException("Deleted User", 404);
        }

        if (user.isLocked()) {
            throw new CustomException("Suspended user", 404);
        }

        // 권한 부여
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isVerified()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_UNVERIFIED_USER"));
        }

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
