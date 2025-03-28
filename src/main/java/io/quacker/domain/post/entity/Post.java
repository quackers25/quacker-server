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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "like_count")
    @Builder.Default
    private int likeCount = 0;

    @Column(name = "repost_count")
    @Builder.Default
    private int repostCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_post_id")
    private Post originPost;

    @Version
    private Long version;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HashtagPost> hashtagPosts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<PostMention> postMentions = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
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

    // 게시글 업데이트
    public void updateText(String newText) {
        this.text = newText;
    }

    // 좋아요 수 증가
    public void incrementLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소
    public void decrementLikeCount() {

        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 이미지 추가
    public void addImages(List<PostImage> images) {
        if (this.postImages == null) {
            this.postImages = new ArrayList<>();
        }

        this.postImages.addAll(images);
    }

    public void addHashtagPost(HashtagPost hashtagPost) {
        if (this.hashtagPosts == null) {
            this.hashtagPosts = new ArrayList<>();
        }
        this.hashtagPosts.add(hashtagPost);
    }

    public void removeHashtagPost(HashtagPost hashtagPost) {
        if (this.hashtagPosts != null) {
            this.hashtagPosts.remove(hashtagPost);
        }
    }

    public List<HashtagPost> getHashtagPosts() {
        if (this.hashtagPosts == null) {
            this.hashtagPosts = new ArrayList<>();
        }
        return hashtagPosts;
    }

    public void addPostMention(PostMention postMention) {
        this.getPostMentions().add(postMention);
    }

    public void addLike(PostLike postLike) {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        this.likes.add(postLike);
    }

    public void removeLike(PostLike postLike) {
        if (this.likes != null) {
            this.likes.remove(postLike);
        }
    }

    public List<PostLike> getLikes() {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        return likes;
    }

}