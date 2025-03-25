package io.quacker.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DefaultCacheRepository {

    boolean exisits(String key);

    Optional<String> get(String key);

    List<Object> getAllKeys();

    String setex(String key, Date exp, String value);

    String delete(String key);
}
