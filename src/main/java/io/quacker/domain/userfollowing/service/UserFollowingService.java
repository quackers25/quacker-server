package io.quacker.domain.userfollowing.service;

import io.quacker.domain.user.dao.UserRepositoy;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.userfollowing.dao.UserFollowingRepository;
import io.quacker.domain.userfollowing.dto.FollowRequestDto;
import io.quacker.domain.userfollowing.dto.FollowResponseDto;
import io.quacker.domain.userfollowing.entitty.UserFollowing;
import io.quacker.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowingService {

    private final UserFollowingRepository userFollowingRepository;
    private final UserRepositoy userRepositoy;

    @Transactional
    public void followingUser(CustomUserDetails customUserDetails,
                              FollowRequestDto followRequestDto) {

        User followee = userRepositoy.findById(followRequestDto.followingUserId())
            .orElseThrow(() -> new CustomException("not found user", HttpStatus.NOT_FOUND.value()));

        UserFollowing userFollowing = new UserFollowing();

        userFollowing.addFollowing(customUserDetails.getUser(), followee);

        userFollowingRepository.save(userFollowing);
    }

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getAllFollowing(CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        List<UserFollowing> userFollowings = user.getUserFollowings();

        List<User> users = userFollowings.stream().map(UserFollowing::getFollowerUser).toList();

        return users.stream().map(FollowResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getAllFollower(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        List<UserFollowing> userFollowers = user.getUserFollowers();

        List<User> users = userFollowers.stream().map(UserFollowing::getFollowingUser).toList();

        return users.stream().map(FollowResponseDto::from).toList();
    }

    @Transactional
    public void unfollowingUser(CustomUserDetails customUserDetails,
                                FollowRequestDto followRequestDto) {

        User followee = userRepositoy.findById(followRequestDto.followingUserId())
            .orElseThrow(() -> new CustomException("not found user", HttpStatus.NOT_FOUND.value()));

        User user = customUserDetails.getUser();

        UserFollowing userFollowing = userFollowingRepository.findByFollowerUserIdAndFollowingUserId(followee.getId(), user.getId())
            .orElseThrow(() -> new CustomException("not found follow", HttpStatus.NOT_FOUND.value()));

        userFollowingRepository.delete(userFollowing);
    }

}
