package io.quacker.domain.user.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.comment.entity.Comment;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postlike.entity.PostLike;
import io.quacker.domain.postmention.entity.PostMention;
import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.userfollowing.entitty.UserFollowing;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "Users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String bio;

    private String avatarImageUrl;

    private boolean isVerified;

    private boolean isLocked;

    private boolean isPrivate;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<PostMention> postMentions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followingUser")
    private List<UserFollowing> userFollowings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followerUser")
    private List<UserFollowing> userFollowers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    public void updateVisibility(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void freeze() { 
        this.isLocked = true; 
    }

    public void unfreeze() { 
        this.isLocked = false; 
    }

    public static User fromCreateDtoWithHashedPassword(UserCreateDto userCreateDto, String hashedPw) {
        return User.builder()
                .email(userCreateDto.email())
                .password(hashedPw)
                .name(userCreateDto.name())
                .bio(userCreateDto.bio())
                .avatarImageUrl(userCreateDto.avatarImageUrl())
                .isPrivate(userCreateDto.isPrivate())
                .build();
    }

    public void updateProfile(String name, String bio, String avatarImageUrl, boolean isLocked, boolean isPrivate) {
        this.name = name;
        this.bio = bio;
        this.avatarImageUrl = avatarImageUrl;
        this.isLocked = isLocked;
        this.isPrivate = isPrivate;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addLike(PostLike like) {
        this.likes.add(like);
    }

    public void removeLike(PostLike like) {
        this.likes.remove(like);
    }

    public static User from(UserCreateDto userCreateDto) {
        return User.builder()
                .email(userCreateDto.email())
                .password(userCreateDto.password())
                .name(userCreateDto.name())
                .bio(userCreateDto.bio())
                .avatarImageUrl(userCreateDto.avatarImageUrl())
                .verified(userCreateDto.verified())
                .isPrivate(userCreateDto.isPrivate())
                .build();
    }
}

