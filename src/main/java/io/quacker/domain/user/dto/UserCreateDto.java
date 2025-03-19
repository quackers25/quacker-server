package io.quacker.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserCreateDto (

        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$")
        String email,

        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String confirmPassword,

        @Pattern(regexp = "^[\\p{L}0-9]{2,16}$")
        String name,

        String bio,

//        @Pattern(regexp = "")
        String avatarImageUrl,

        boolean isPrivate
) {}
