package kr.mybrary.bookservice.booksearch.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookSearchRankingService {

    private static final String RANKING_KEY = "bookSearchRanking";
    private static final int RANKING_INCREMENT_SCORE = 1;

    private final RedisTemplate<String, String> redisTemplate;

    @Async
    public void increaseSearchRankingScore(String keyword) {
        try {
            redisTemplate.opsForZSet().incrementScore(RANKING_KEY, keyword, RANKING_INCREMENT_SCORE);
        } catch (Exception e) {
            log.error("Fail Increase Search Ranking Score At {} Keyword, {}", keyword, e);
        }
    }
}
