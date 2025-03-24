package io.quacker.domain.userfollowing.service;

import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
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
    private final UserRepository userRepository;

    private final UserService userService;

    @Transactional
    public FollowResponseDto followingUser(FollowRequestDto followRequestDto) {

        User user = userService.getCurrentUser();

        User followee = userRepository.findById(followRequestDto.followingUserId())
            .orElseThrow(() -> new CustomException("not found user", HttpStatus.NOT_FOUND.value()));

        UserFollowing userFollowing = new UserFollowing();

        userFollowing.addFollowing(user, followee);

        userFollowingRepository.save(userFollowing);

        return FollowResponseDto.from(followee);
    }

    @Transactional
    public FollowResponseDto unfollowingUser(Long followingUserId) {

        User followee = userRepository.findById(followingUserId)
            .orElseThrow(() -> new CustomException("not found user", HttpStatus.NOT_FOUND.value()));

        User user = userService.getCurrentUser();

        UserFollowing userFollowing = userFollowingRepository.findByFollowerUserIdAndFollowingUserId(followee.getId(), user.getId())
            .orElseThrow(() -> new CustomException("not found follow", HttpStatus.NOT_FOUND.value()));

        userFollowingRepository.delete(userFollowing);

        return FollowResponseDto.from(followee);
    }

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getAllFollowingUserId(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepository.findByFollowingUserId(userId);

        List<User> users = userFollowings.stream().map(UserFollowing::getFollowerUser).toList();

        return users.stream().map(FollowResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponseDto> getAllFollowerByUserId(Long userId) {
        List<UserFollowing> userFollowers = userFollowingRepository.findByFollowerUserId(userId);

        List<User> users = userFollowers.stream().map(UserFollowing::getFollowingUser).toList();

        return users.stream().map(FollowResponseDto::from).toList();
    }

}
