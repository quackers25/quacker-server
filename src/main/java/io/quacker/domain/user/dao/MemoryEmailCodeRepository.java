package io.quacker.domain.user.dao;

import io.quacker.common.dao.EmailCodeRepository;
import io.quacker.domain.user.dto.EmailCodeItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Slf4j
@EnableScheduling
public class MemoryEmailCodeRepository implements EmailCodeRepository {

    private final Map<String, EmailCodeItem> mem = new HashMap<>();

    @Override
    public boolean exisits(String key) {
        return mem.get(key) != null;
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(mem.get(key).getCode());
    }

    @Override
    public List<Object> getAllKeys() {
        return Arrays.asList(mem.keySet().toArray());
    }

    @Override
    public String setex(String key, Date exp, String value) {
        var save = EmailCodeItem.builder()
                .code(value)
                .exp(exp)
                .build();
        mem.put(key, save);
        return save.getCode();
    }

    @Override
    public String delete(String key) {
        return mem.remove(key).getCode();
    }

    @Scheduled(fixedDelay = 10000)
    public void deleteExpiredItem() {
        Date now = new Date();
        mem.entrySet().removeIf(entry-> {
            log.info("["+ entry.getKey() + "]" + "   만료시간: "+  entry.getValue().getExp().toString());
            return entry.getValue().getExp().before(now);
        });
    }
}
