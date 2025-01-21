package com.example.util.Redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate template;
    
    public boolean isMemberOfSet(String key, String value) {
        return template.opsForSet().isMember(key, value);
    }
    
    public void addToSet(String key, String value) {
        template.opsForSet().add(key, value);
    }
    
    public void removeFromSet(String key, String value) {
        template.opsForSet().remove(key, value);
    }
}
