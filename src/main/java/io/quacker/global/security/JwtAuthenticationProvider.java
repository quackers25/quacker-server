package io.quacker.global.security;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.admin.dto.AdminDetails;
import io.quacker.domain.admin.service.AdminDetailsService;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final AdminDetailsService adminDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPw = authentication.getCredentials().toString();

        UserDetails details;

        if (authentication.getPrincipal() instanceof AdminDetails) {
            details = adminDetailsService.loadUserByUsername(email);
        } else {
            details = customUserDetailsService.loadUserByUsername(email);
        }

        if (!passwordEncoder.matches(rawPw, details.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new JwtAuthenticationToken(details.getAuthorities(), details);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

