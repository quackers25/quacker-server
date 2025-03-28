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
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String hint;

    @Setter
    private Date deletedAt;

    @Setter
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
    @OneToMany(mappedBy = "user")
    private List<PostLike> likes = new ArrayList<>();

    public static User fromCreateDtoWithHashedPassword(UserCreateDto userCreateDto,
                                                       String hashedPw) {
        return User.builder()
            .email(userCreateDto.email())
            .password(hashedPw)
            .name(userCreateDto.name())
            .bio(userCreateDto.bio())
            .avatarImageUrl(userCreateDto.avatarImageUrl())
            .isPrivate(userCreateDto.isPrivate())
            .isVerified(false)
            .build();
    }

    public void updateVisibility(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void freeze() {
        this.isLocked = false;
    }

    public void unfreeze() {
        this.isLocked = true;
    }

    public void updateProfile(String name, String bio, String avatarImageUrl, boolean isLocked,
                              boolean isPrivate) {
        this.name = name;
        this.bio = bio;
        this.avatarImageUrl = avatarImageUrl;
        this.isLocked = isLocked;
        this.isPrivate = isPrivate;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addPostMention(PostMention postMention) {
        this.postMentions.add(postMention);
    }
}

