package io.quacker.common.dao;

import org.springframework.scheduling.annotation.Scheduled;

public interface EmailCodeRepository extends DefaultCacheRepository {

    void deleteExpiredItem();
}
