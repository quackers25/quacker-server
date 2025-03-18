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

    private boolean verified;

    private boolean isLocked;

    private boolean isPrivate;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostMention> postMentions = new ArrayList<>();

    @OneToMany(mappedBy = "followingUser")
    private List<UserFollowing> userFollowings = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser")
    private List<UserFollowing> userFollowers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLike> likes = new ArrayList<>();


    public User from(UserCreateDto userCreateDto) {
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

