package io.quacker.domain.hashtagpost.entity;

import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_hashtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public HashtagPost(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
        hashtag.addHashtagPost(this);
    }

    public static HashtagPost of(Post post, Hashtag hashtag) {
        return new HashtagPost(post, hashtag);
    }
}
