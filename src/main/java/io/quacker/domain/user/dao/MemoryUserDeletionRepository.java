package io.quacker.domain.user.dao;

import io.quacker.common.dao.UserDeletionRepository;
import io.quacker.domain.user.dto.DeletionItem;
import io.quacker.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
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
        var item = DeletionItem.builder()
                .userId(key)
                .exp(exp)
                .build();
        mem.put(key, item);
        return item.getUserId().toString();
    }

    @Override
    public String delete(String key) {
        var item = mem.remove(key);
        if (item == null)
            return null;
        return item.getUserId().toString();
    }

    @Override
    public List<String> getAllExpiredKeys(Date now) {
        List<String> result = new ArrayList<>();
        for (String key : mem.keySet()) {
            if (mem.get(key).getExp().before(now)){
                result.add(key);
            }
        }
        return result;
    }

}
