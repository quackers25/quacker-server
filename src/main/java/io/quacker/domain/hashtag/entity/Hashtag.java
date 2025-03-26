package io.quacker.domain.hashtag.entity;

import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "post_count")
    private int postCount = 0;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Hashtag(String name) {
        this.name = name;
    }

    public void incrementPostCount() {
        this.postCount++;
    }

    public void decrementPostCount() {
        if (this.postCount > 0) {
            this.postCount--;
        }
    }

    public void addHashtagPost(HashtagPost hashtagPost) {
        this.hashtagPosts.add(hashtagPost);
        incrementPostCount();
    }

    public void removeHashtagPost(HashtagPost hashtagPost) {
        if (this.hashtagPosts.remove(hashtagPost)) {
            decrementPostCount();
        }
    }
}