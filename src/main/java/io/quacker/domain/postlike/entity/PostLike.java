package io.quacker.domain.postlike.entity;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
