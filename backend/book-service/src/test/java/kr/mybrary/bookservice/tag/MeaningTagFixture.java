package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MeaningTag.MeaningTagBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MeaningTagFixture {

    COMMON_MEANING_TAG(1L, "의미있는 책", 1, "LOGIN_USER_ID"),
    COMMON_MEANING_REGISTERED_COUNT_10_TAG(2L, "재미있는 책", 10, "LOGIN_USER_ID");

    private final Long id;
    private final String quote;
    private final int registeredCount;
    private final String createdBy;

    public MeaningTagBuilder getMeaningTagBuilder() {
        return MeaningTag.builder()
                .id(id)
                .quote(quote)
                .registeredCount(registeredCount)
                .createdBy(createdBy);
    }

    public MeaningTag getMeaningTag() {
        return MeaningTag.builder()
                .id(id)
                .quote(quote)
                .registeredCount(registeredCount)
                .createdBy(createdBy)
                .build();
    }
}
