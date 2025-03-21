package io.quacker.domain.postlike.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PostLikes")
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        user.addLike(this);
        post.incrementLikeCount();
    }

    public static PostLike of(Post post, User user) {
        return new PostLike(post, user);
    }

    public void unlike() {
        user.removeLike(this);
        post.decrementLikeCount();
    }
}
