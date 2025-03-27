package io.quacker.common.dao;

public interface JwtRepository extends DefaultCacheRepository {

    void deleteExpiredItem();
}
