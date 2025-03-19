package io.quacker.domain.userfollowing.entitty;

import static org.assertj.core.api.Assertions.assertThat;

import io.quacker.domain.user.entity.User;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class UserFollowingTest {

    @Test
    void addFollowTest() {
        User requestUser = User.builder()
            .email("example@google.com")
            .password("password")
            .name("request")
            .bio("bio example")
            .userFollowers(new ArrayList<>())
            .userFollowings(new ArrayList<>())
            .build();

        User followerUser = User.builder()
            .email("example2@google.com")
            .password("password")
            .name("following")
            .bio("bio example")
            .userFollowers(new ArrayList<>())
            .userFollowings(new ArrayList<>())
            .build();

        UserFollowing userFollowing = new UserFollowing();

        userFollowing.addFollowing(requestUser, followerUser);

        assertThat(userFollowing.getFollowerUser()).isNotNull();
        assertThat(userFollowing.getFollowingUser()).isNotNull();

        assertThat(requestUser.getUserFollowings().size()).isEqualTo(1);
        assertThat(followerUser.getUserFollowers().size()).isEqualTo(1);
    }

}