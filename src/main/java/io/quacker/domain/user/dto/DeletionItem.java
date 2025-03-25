package io.quacker.domain.user.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class DeletionItem {

    private Object userId;
    private Date exp;

    public Object getUserId() { return userId; }

    public Date getExp() { return exp; }

}
