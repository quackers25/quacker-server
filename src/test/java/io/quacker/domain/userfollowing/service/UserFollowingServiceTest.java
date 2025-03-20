package io.quacker.domain.userfollowing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
import io.quacker.domain.userfollowing.dao.UserFollowingRepository;
import io.quacker.domain.userfollowing.dto.FollowRequestDto;
import io.quacker.domain.userfollowing.dto.FollowResponseDto;
import io.quacker.domain.userfollowing.entitty.UserFollowing;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserFollowingServiceTest {

    @InjectMocks
    UserFollowingService userFollowingService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    UserFollowingRepository userFollowingRepository;

    User user;

    User followee;

    User followee2;

    @BeforeEach
    void init() {
        user = User.builder()
            .id(1L)
            .name("name")
            .password("password")
            .email("email")
            .bio("bio")
            .avatarImageUrl("https://example.com/image.jpg")
            .build();

        followee = User.builder()
            .id(2L)
            .name("followee")
            .password("password")
            .email("email")
            .bio("bio")
            .avatarImageUrl("https://example.com/image.jpg")
            .build();

        followee2 = User.builder()
            .id(3L)
            .name("followee")
            .password("password")
            .email("email")
            .bio("bio")
            .avatarImageUrl("https://example.com/image.jpg")
            .build();
    }

    @Test
    @DisplayName("팔로우 추가 테스트")
    void addFollowingUserTest() {

        when(userService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(followee.getId())).thenReturn(Optional.of(followee));

        FollowRequestDto followRequestDto = new FollowRequestDto(followee.getId());

        userFollowingService.followingUser(followRequestDto);

        List<UserFollowing> userFollowings = user.getUserFollowings();
        List<UserFollowing> userFollowers = followee.getUserFollowers();

        assertThat(userFollowings.size()).isEqualTo(1);
        assertThat(userFollowers.size()).isEqualTo(1);

        verify(userFollowingRepository, times(1)).save(any(UserFollowing.class));
    }

    @Test
    @DisplayName("팔로우 삭제 테스트")
    void unfollowingUserTest() {
        UserFollowing userFollowing = UserFollowing.builder()
            .followingUser(user)
            .followerUser(followee)
            .build();

        when(userService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(followee.getId())).thenReturn(Optional.of(followee));
        when(userFollowingRepository.findByFollowerUserIdAndFollowingUserId(followee.getId(), user.getId())).thenReturn(Optional.of(userFollowing));

        userFollowingService.unfollowingUser(followee.getId());

        verify(userFollowingRepository, times(1)).delete(userFollowing);
    }


    @Test
    @DisplayName("팔로잉 목록 조회 테스트")
    void getFollowingListByUserIdTest() {

        List<UserFollowing> userFollowings = new ArrayList<>(List.of(
            UserFollowing.builder()
                .followingUser(user)
                .followerUser(followee)
                .build(),
            UserFollowing.builder()
                .followingUser(user)
                .followerUser(followee2)
                .build()));

        when(userFollowingRepository.findByFollowingUserId(user.getId())).thenReturn(userFollowings);

        List<FollowResponseDto> followingList = userFollowingService.getAllFollowingUserId(user.getId());

        assertThat(followingList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("팔로어 목록 조회 테스트")
    void getFollowerListByUserIdTest() {

        List<UserFollowing> userFollowings = new ArrayList<>(List.of(
            UserFollowing.builder()
                .followingUser(user)
                .followerUser(followee)
                .build(),
            UserFollowing.builder()
                .followingUser(user)
                .followerUser(followee2)
                .build()));

        when(userFollowingRepository.findByFollowerUserId(user.getId())).thenReturn(userFollowings);

        List<FollowResponseDto> followerList = userFollowingService.getAllFollowerByUserId(user.getId());

        assertThat(followerList.size()).isEqualTo(2);
    }
}