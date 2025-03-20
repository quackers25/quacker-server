package io.quacker.domain.post.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.comment.entity.Comment;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.postimage.entity.PostImage;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postmention.entity.PostMention;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Builder.Default
    private int likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    private Long version;

    private int repostCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post originPost;

    @OneToMany(mappedBy = "originPost")
    private List<Post> replyPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = jakarta.persistence.CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostMention> postMentions = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = jakarta.persistence.CascadeType.ALL)
    private List<PostLike> likes = new ArrayList<>();

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 게시글 업데이트
    public void updateText(String newText) {
        this.text = newText;
    }
}