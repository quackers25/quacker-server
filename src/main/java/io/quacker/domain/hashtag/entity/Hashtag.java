package io.quacker.domain.hashtag.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int recentPostCount;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    private Hashtag(String name) {
        this.name = name;
        this.recentPostCount = 0;
    }

    public static Hashtag of(String name) {
        return new Hashtag(name);
    }

    public void incrementRecentPostCount() {
        this.recentPostCount++;
    }

    public void addHashtagPost(HashtagPost hashtagPost) {
        this.hashtagPosts.add(hashtagPost);
    }
}