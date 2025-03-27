package io.quacker.domain.admin.service;

import io.quacker.domain.admin.dao.AdminRepository;
import io.quacker.domain.admin.dto.AdminDetails;
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
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var admin = adminRepository.findByUsername(username)
                        .orElseThrow(()-> new CustomException("유저가 존재하지않습니다", 404));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return AdminDetails.builder()
                .id(admin.getId())
                .username(username)
                .password(admin.getPassword())
                .authoritie(authorities)
                .build();
    }
}
