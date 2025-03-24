package io.quacker.domain.auth.dto;

import lombok.Builder;

import java.util.Date;

/**
 * 토큰 리스트에 저장되기 위한 인터페이스입니다.
 * 토큰 객체가 아니라. 토큰의 일부 정보를 저장합니다.
 */
@Builder
public class JwtItem {

    private Object tokenId;
    private Date exp;

    public Object getTokenId() { return tokenId; }

    public Date getExp() { return exp; }

}


