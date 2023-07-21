package kr.mybrary.bookservice.tag.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import kr.mybrary.bookservice.tag.MeaningTagDtoTestData;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeaningTagServiceTest {

    @InjectMocks
    private MeaningTagService meaningTagService;

    @Mock
    private MeaningTagRepository meaningTagRepository;

    @Mock
    private MyBookMeaningTagRepository myBookMeaningTagRepository;

    @DisplayName("마이북에 의미 태그를 설정한다.")
    @Test
    void assignMeaningTag() {

        // given
        MeaningTagAssignServiceRequest request = MeaningTagDtoTestData.createMeaningTagAssignServiceRequest();
        MeaningTag meaningTag = MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag();
        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag();

        int originalRegisteredCount = meaningTag.getRegisteredCount();

        given(meaningTagRepository.findByQuote(any())).willReturn(Optional.of(meaningTag));
        given(myBookMeaningTagRepository.findByMyBook(any())).willReturn(
                Optional.of(myBookMeaningTag));
        given(myBookMeaningTagRepository.save(any())).willReturn(any());

        // when
        meaningTagService.assignMeaningTag(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findByQuote(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).findByMyBook(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).save(any()),
                () -> assertThat(meaningTag.getRegisteredCount()).isEqualTo(
                        originalRegisteredCount + 1),
                () -> {
                    assertThat(myBookMeaningTag).isNotNull();
                    assertThat(myBookMeaningTag.getMeaningTagColor()).isEqualTo(
                            request.getColorCode());
                },
                () -> {

                    assertThat(myBookMeaningTag).isNotNull();
                    assertThat(myBookMeaningTag.getMeaningTag().getQuote()).isEqualTo(
                            meaningTag.getQuote());
                }
        );
    }
}