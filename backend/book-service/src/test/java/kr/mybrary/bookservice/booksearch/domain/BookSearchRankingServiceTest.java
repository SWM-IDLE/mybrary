package kr.mybrary.bookservice.booksearch.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.times;

import java.util.HashSet;
import java.util.Set;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchRankingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookSearchRankingServiceTest {

    @InjectMocks
    private BookSearchRankingService bookSearchRankingService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    private static final String RANKING_KEY = "bookSearchRanking";
    private static final String BOOK_SEARCH_KEYWORD = "test_keyword";

    @DisplayName("검색 키워드 랭킹 점수 증가한다.")
    @Test
    public void increaseSearchRankingScore() {

        // given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        // when
        bookSearchRankingService.increaseSearchRankingScore(BOOK_SEARCH_KEYWORD);

        // then
        verify(zSetOperations).incrementScore(RANKING_KEY, BOOK_SEARCH_KEYWORD, 1.0);
    }

    @DisplayName("검색 키워드 랭킹 리스트를 조회한다.")
    @Test
    public void getBookSearchKeywordRankingList() {

        // given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        Set<ZSetOperations.TypedTuple<String>> mockSet = new HashSet<>(Set.of(
                new DefaultTypedTuple<>("keyword1", 1.0),
                new DefaultTypedTuple<>("keyword2", 2.0),
                new DefaultTypedTuple<>("keyword3", 3.0),
                new DefaultTypedTuple<>("keyword4", 4.0),
                new DefaultTypedTuple<>("keyword5", 5.0),
                new DefaultTypedTuple<>("keyword6", 6.0)
        ));

        when(zSetOperations.reverseRangeWithScores(RANKING_KEY, 0, 9)).thenReturn(mockSet);

        // when
        BookSearchRankingResponse response = bookSearchRankingService.getBookSearchKeywordRankingList();

        // then
        assertAll(
                () -> assertThat(response.getBookSearchKeywords().size()).isEqualTo(6),
                () -> verify(zSetOperations, times(1)).reverseRangeWithScores(RANKING_KEY, 0, 9)
        );
    }

    @DisplayName("검색 히스토리가 없을 경우, 검색 키워드 랭킹 리스트를 조회시, 빈 리스트를 반환한다.")
    @Test
    public void returnEmptyListWhenNotExistSearchHistory() {

        // given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRangeWithScores(RANKING_KEY, 0, 9))
                .thenReturn(new HashSet<>());

        // when
        BookSearchRankingResponse response = bookSearchRankingService.getBookSearchKeywordRankingList();

        // then
        assertAll(
                () -> assertThat(response.getBookSearchKeywords()).isEmpty(),
                () -> verify(zSetOperations, times(1)).reverseRangeWithScores(RANKING_KEY, 0, 9)
        );
    }
}





