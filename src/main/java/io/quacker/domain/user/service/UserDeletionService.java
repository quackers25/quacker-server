package io.quacker.domain.user.service;

import io.quacker.common.dao.UserDeletionRepository;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.dto.DeletionItem;
import io.quacker.domain.user.entity.User;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class UserDeletionService {

    private final UserRepository userRepository;
    private final UserDeletionRepository userDeletionRepository;


    public void addRequest(Long userId, Date exp) {
        userDeletionRepository.setex(String.valueOf(userId), exp, "");
    }

    public String abortRequest(String key) {
        return userDeletionRepository.delete(key);
    }

    /**
     * 배치나, 스케줄 추가하여 만료토큰 정기적으로 삭제?
     * 현시간 부로 만료된 토큰을 모두 제거
     * 10초마다 삭제
     */
    @Scheduled(fixedDelay = 10000)
    public void deleteExpiredItem() {
        // 저장소의 모든 키를 순회하며 만료된 요청을 삭제
        for (Object key : userDeletionRepository.getAllKeys()) {
            DeletionItem item = (DeletionItem)key;

            if (item.getExp().before(new Date())) {
                User user = userRepository.findById((Long)item.getUserId())
                        .orElseThrow(()-> new CustomException("", HttpStatus.BAD_REQUEST.value()));
                userRepository.delete(user);
                userDeletionRepository.delete((String) item.getUserId());
            }
        }
    }
}
