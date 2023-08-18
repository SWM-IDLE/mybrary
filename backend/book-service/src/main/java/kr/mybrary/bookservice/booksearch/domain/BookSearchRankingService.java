package kr.mybrary.bookservice.booksearch.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchRankingResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchRankingResponse.BookSearchRanking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookSearchRankingService {

    private static final String RANKING_KEY = "bookSearchRanking";
    private static final int RANKING_INCREMENT_SCORE = 1;
    private static final int RANKING_START = 0;
    private static final int RANKING_END = 9;

    private final RedisTemplate<String, String> redisTemplate;

    @Async
    public void increaseSearchRankingScore(String keyword) {
        try {
            redisTemplate.opsForZSet().incrementScore(RANKING_KEY, keyword, RANKING_INCREMENT_SCORE);
        } catch (Exception e) {
            log.error("Fail Increase Search Ranking Score At {} Keyword, {}", keyword, e);
        }
    }

    public BookSearchRankingResponse getBookSearchKeywordRankingList() {
        Set<TypedTuple<String>> rankingSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores(RANKING_KEY, RANKING_START, RANKING_END);

        List<BookSearchRanking> bookSearchKeywordRanking = Optional.ofNullable(rankingSet)
                .map(keywords -> keywords.stream()
                        .map(keyword -> BookSearchRanking.of(keyword.getValue(), keyword.getScore()))
                        .toList())
                .orElseGet(List::of);

        return BookSearchRankingResponse.of(bookSearchKeywordRanking);
    }
}
