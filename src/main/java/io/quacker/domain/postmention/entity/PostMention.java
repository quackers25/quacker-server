package io.quacker.domain.postmention.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class PostMention extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public void setPost(Post post) {
        this.post = post;
        post.addPostMention(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.addPostMention(this);
    }

}
