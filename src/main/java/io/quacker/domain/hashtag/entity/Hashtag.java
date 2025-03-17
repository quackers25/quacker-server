package io.quacker.domain.hashtag.entity;

import io.quacker.domain.hashtagpost.entity.HashtagPost;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int recentPostCount;

    @OneToMany(mappedBy = "hashtag")
    private List<HashtagPost> hashtagPosts = new ArrayList<>();
}