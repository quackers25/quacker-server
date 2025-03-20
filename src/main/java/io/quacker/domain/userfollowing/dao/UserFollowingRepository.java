package io.quacker.domain.userfollowing.dao;

import io.quacker.domain.userfollowing.entitty.UserFollowing;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

    Optional<UserFollowing> findByFollowerUserIdAndFollowingUserId(Long followerUserId,
                                                                   Long followingUserId);

    List<UserFollowing> findByFollowerUserId(Long userId);

    List<UserFollowing> findByFollowingUserId(Long userId);
}
