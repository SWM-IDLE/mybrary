package kr.mybrary.bookservice.tag.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeaningTagDtoMapperTest {

    @DisplayName("MeaningTag 엔티티를 MeaningTagElementResponse로 매핑한다.")
    @Test
    void entityToMeaningTagElementResponse() {

        // given
        MeaningTag meaningTag = MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag();

        // when
        MeaningTagElementResponse meaningTagElementResponse = MeaningTagDtoMapper.INSTANCE.entityToMeaningTagElementResponse(meaningTag);

        // then
        assertAll(
                () -> assertEquals(meaningTag.getId(), meaningTagElementResponse.getId()),
                () -> assertEquals(meaningTag.getQuote(), meaningTagElementResponse.getQuote()),
                () -> assertEquals(meaningTag.getRegisteredCount(), meaningTagElementResponse.getRegisteredCount())
        );
    }
}