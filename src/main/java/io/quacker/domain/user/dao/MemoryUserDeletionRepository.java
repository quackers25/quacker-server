package io.quacker.domain.user.dao;

import io.quacker.common.dao.UserDeletionRepository;
import io.quacker.domain.user.dto.DeletionItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Slf4j
@EnableScheduling
public class MemoryUserDeletionRepository implements UserDeletionRepository {

    private final Map<String, DeletionItem> mem = new HashMap<>();

    @Override
    public boolean exisits(String key) { return mem.containsKey(key); }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(mem.get(key).getUserId().toString());
    }

    @Override
    public List<Object> getAllKeys() {
        return Arrays.asList(mem.keySet().toArray());
    }

    @Override
    public String setex(String key, Date exp, String value) {
        var item = mem.put(key, DeletionItem.builder()
                .userId(key)
                .exp(exp)
                .build());
        return item.getUserId().toString();
    }

    @Override
    public String delete(String key) {
        return mem.remove(key).getUserId().toString();
    }

    @Override
    @Scheduled(fixedDelay = 10000)
    public void deleteExpiredItem() {
        Date now = new Date();
        mem.entrySet().removeIf(entry-> {
            log.info("["+ entry.getKey() + "]" + "   만료시간: "+  entry.getValue().getExp().toString());
            return entry.getValue().getExp().before(now);
        });
    }
}
