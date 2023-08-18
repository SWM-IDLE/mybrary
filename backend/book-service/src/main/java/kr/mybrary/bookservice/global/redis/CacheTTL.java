package kr.mybrary.bookservice.global.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheTTL {

    ONE_MONTH(60 * 60 * 24 * 30),
    ONE_WEEK(60 * 60 * 24 * 7),
    ONE_DAY(60 * 60 * 24),
    ONE_HOUR(60 * 60),
    ONE_MIN(60);

    private final int expireTimeSeconds;
}
