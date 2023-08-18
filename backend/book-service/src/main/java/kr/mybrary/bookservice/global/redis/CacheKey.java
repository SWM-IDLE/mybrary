package kr.mybrary.bookservice.global.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheKey {

    BOOK_LIST_BY_CATEGORY("bookSearch", "bookListByCategory", CacheTTL.ONE_WEEK.getExpireTimeSeconds()),
    BOOK_LIST_BY_SEARCH_KEYWORD("bookSearch", "bookListBySearchKeyword", CacheTTL.ONE_WEEK.getExpireTimeSeconds());

    private final String prefix;
    private final String key;
    private final int expireTimeSeconds;

}
