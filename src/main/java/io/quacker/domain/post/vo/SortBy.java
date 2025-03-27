package io.quacker.domain.post.vo;

import org.springframework.data.domain.Sort;

public enum SortBy {
    NEWEST("createdAt"),
    LIKES("likeCount");

    private final String property;

    SortBy(String property) {
        this.property = property;
    }

    public Sort getSort() {
        return Sort.by(Sort.Direction.DESC, property);
    }
}
