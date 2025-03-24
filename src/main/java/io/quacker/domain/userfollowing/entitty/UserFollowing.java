package io.quacker.domain.userfollowing.entitty;

import io.quacker.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserFollowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User followerUser;

    public void addFollowing(User followingUser, User followerUser) {
        this.followingUser = followingUser;
        followingUser.getUserFollowings().add(this);
        this.followerUser = followerUser;
        followerUser.getUserFollowers().add(this);
    }

}
