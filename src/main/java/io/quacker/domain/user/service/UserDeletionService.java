package io.quacker.domain.user.service;

import io.quacker.common.dao.UserDeletionRepository;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDeletionService {

    private final UserDeletionRepository userDeletionRepository;
    private final UserRepository userRepository;

    public void addRequest(Long userId, Date exp) {
        userDeletionRepository.setex(String.valueOf(userId), exp, "");
    }

    public void abortRequest(String key) {
        if (!userDeletionRepository.exisits(key))
            throw new CustomException("취소할 삭제요청이 없습니다.", HttpStatus.BAD_REQUEST.value());
        userDeletionRepository.delete(key);
    }

    /**
     * 완전 삭제가 아님
     */
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void deleteExpiredItem() {
        Date now = new Date();
        List<String> keys =  userDeletionRepository.getAllExpiredKeys(now);

        if(!keys.isEmpty())
            log.info(String.format("%d users deleted.", keys.size()));

        for (String key : keys) {
            var id = Long.parseLong(key);
            if (!userRepository.existsById(id))
                continue;

            var user = userRepository.findById(id).get();

            // 소프트 삭제 처리
            user.setDeletedAt(now);
            /*
            다른 개인정보 필드 마스킹 또는 지우기
             */

            // 요청 캐시 삭제
            userDeletionRepository.delete(key);

            //TODO 삭제 대상자의 토큰 모두 만료 필요
        }
    }
}
