package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MeaningTagFixture {

    COMMON_MEANING_TAG(1L, "의미있는 책", 1, "LOGIN_USER_ID");

    private final Long id;
    private final String quote;
    private final int registeredCount;
    private final String createdBy;

    public MeaningTag getMeaningTag() {
        return MeaningTag.builder()
                .id(id)
                .quote(quote)
                .registeredCount(registeredCount)
                .createdBy(createdBy)
                .build();
    }

    public MeaningTag getMeaningTag(Long id, int registeredCount) {
        return MeaningTag.builder()
                .id(id)
                .quote(quote)
                .registeredCount(registeredCount)
                .createdBy(createdBy)
                .build();
    }
}
