package io.quacker.domain.user.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class UserDeletionRequest {
    private Long userId;
    private String reason;
    private Date exp;


}
