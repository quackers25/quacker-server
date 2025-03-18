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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
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

    private PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static PostLike of(User user, Post post) {
        PostLike postLike = new PostLike(user, post);
        post.incrementLikeCount();
        user.addLike(postLike);
        return postLike;
    }

    public void unlike() {
        this.post.decrementLikeCount();
        this.user.removeLike(this);
    }
}
