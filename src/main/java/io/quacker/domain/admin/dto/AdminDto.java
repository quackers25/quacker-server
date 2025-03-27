package io.quacker.domain.admin.dto;

import io.quacker.domain.admin.entity.Admin;
import lombok.Builder;

@Builder
public record AdminDto(
    Long id,
    String username,
    String role
){
    public static AdminDto from(Admin admin) {
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .role(admin.getRole())
                .build();
    }
}