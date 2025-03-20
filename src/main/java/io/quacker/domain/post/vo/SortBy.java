package io.quacker.domain.post.vo;

import org.springframework.data.domain.Sort;

public enum SortBy {
    NEWEST(Sort.by(Sort.Direction.DESC, "createdAt")),  // 최신순 정렬
    LIKES(Sort.by(Sort.Direction.DESC, "likeCount"));   // 좋아요순 정렬

    private final Sort sort;

    SortBy(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return this.sort;
    }
}