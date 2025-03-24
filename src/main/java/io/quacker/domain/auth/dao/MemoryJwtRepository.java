package io.quacker.domain.auth.dao;

import io.quacker.common.dao.JwtRepository;
import io.quacker.domain.auth.dto.JwtItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemoryJwtRepository implements JwtRepository {

    //TODO 재발급 limit을 위한 카운트?
    private final Map<String, JwtItem> mem = new HashMap<>();

    @Override
    public boolean exisits(String key) {
        return mem.get(key) != null;
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(mem.get(key).getTokenId().toString());
    }

    @Override
    public List<Object> getAllKeys() {
        return Arrays.asList(mem.keySet().toArray());
    }

    @Override
    public String setex(String key, Date exp, String value) {
        var item = mem.put(key, JwtItem.builder()
                .exp(exp)
                .tokenId(value)
                .build());

        return item.getTokenId().toString();
    }

    @Override
    public String delete(String key) {
        return mem.remove(key).getTokenId().toString();
    }

    public void deleteExpiredItem() {
//        log.info("Current cnt : " + mem.size());
//        mem.entrySet().removeIf(entry->entry.getValue().isBanned());
        Date now = new Date();
        mem.entrySet().removeIf(entry-> {
            log.info("["+ entry.getKey() + "]" + "   만료시간: "+  entry.getValue().getExp().toString());
            return entry.getValue().getExp().before(now);
        });
    }
}
