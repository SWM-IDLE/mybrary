package kr.mybrary.userservice.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public void set(String key, Object object, Duration duration) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(object.getClass()));
        redisTemplate.opsForValue().set(key, object, duration);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setBlackList(String key, Object object, Duration duration) {
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(object.getClass()));
        redisBlackListTemplate.opsForValue().set(key, object, duration);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public void deleteBlackList(String key) {
        redisBlackListTemplate.delete(key);
    }

    public boolean hasBlackListKey(String key) {
        return redisBlackListTemplate.hasKey(key);
    }

}
