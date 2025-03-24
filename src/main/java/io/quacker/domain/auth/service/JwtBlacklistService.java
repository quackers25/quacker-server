package io.quacker.domain.auth.service;

import io.quacker.common.dao.JwtRepository;
import io.quacker.common.util.JwtTokenUtil;
import io.quacker.common.dao.DefaultCacheRepository;
import io.quacker.domain.auth.dao.MemoryJwtRepository;
import io.quacker.domain.auth.dto.JwtItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 리프레시 토큰 블랙리스트 서비스입니다.
 */
@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtRepository jwtRepository;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 토큰이 리스트에 포함되며, 만료 되지않았고, 유효한지 확인합니다.
     * @param token
     * @return 유효성
     */
    public boolean validateToken(String token) {
        String key = jwtTokenUtil.extractId(token);
        if (jwtRepository.exisits(key)){
            return false;
        }

        return true;
    }

    /**
     * 토큰을 리스트에 등록합니다.
     * @param token
     */
    public void registerToken(String token) {
        String key = jwtTokenUtil.extractId(token);
        log.info(key);
        jwtRepository.setex(
                key,
                jwtTokenUtil.extractExpiration(token),
                jwtTokenUtil.extractUserId(token).toString()
        );
    }

    /**
     * 배치나, 스케줄 추가하여 만료토큰 정기적으로 삭제?
     * 현시간 부로 만료된 토큰을 모두 제거
     * 10초마다 삭제
     */
    @Scheduled(fixedDelay = 10000)
    public void deleteExpiratedJwtItem() {
        ((MemoryJwtRepository) jwtRepository).deleteExpiratedItem();
    }
}
