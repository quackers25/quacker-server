package io.quacker.domain.post.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.comment.entity.Comment;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.postimage.entity.PostImage;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postmention.entity.PostMention;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private int likeCount;

    private int repostCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post originPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "originPost")
    private List<Post> reposts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostMention> postMentions = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    /** ✅ 리트윗 수 증가 */
    public void incrementRepostCount() {
        this.repostCount++;
    }

    /** ✅ 리트윗 수 감소 */
    public void decrementRepostCount() {
        if (this.repostCount > 0) {
            this.repostCount--;
        }
    }
}