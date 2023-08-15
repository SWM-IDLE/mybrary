package kr.mybrary.apigatewayserver.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
