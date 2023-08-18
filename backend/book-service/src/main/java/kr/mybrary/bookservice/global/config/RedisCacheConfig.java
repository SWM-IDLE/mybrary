package kr.mybrary.bookservice.global.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import kr.mybrary.bookservice.global.redis.CacheKey;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cf) {

        RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(cf);

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofDays(1L))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = Arrays.stream(CacheKey.values())
                .collect(Collectors.toMap(
                        CacheKey::getKey,
                        cacheKey -> RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(cacheKey.getExpireTimeSeconds()))
                                .prefixCacheNameWith(cacheKey.getPrefix() + "::")
                ));

        return builder.cacheDefaults(configuration).withInitialCacheConfigurations(cacheConfigurations).build();
    }
}
