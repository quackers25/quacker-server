package io.quacker.common.dao;

import java.util.Date;
import java.util.List;

public interface UserDeletionRepository extends DefaultCacheRepository{
    List<String> getAllExpiredKeys(Date now);
}
