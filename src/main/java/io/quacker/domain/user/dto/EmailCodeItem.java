package io.quacker.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class EmailCodeItem {
    private String code;
    private Date exp;
}
