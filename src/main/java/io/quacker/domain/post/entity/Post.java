package io.quacker.domain.post.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.comment.entity.Comment;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.postimage.entity.PostImage;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postmention.entity.PostMention;
import io.quacker.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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
    private List<Post> replyPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostMention> postMentions = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();
}