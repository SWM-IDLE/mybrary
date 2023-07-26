package kr.mybrary.bookservice.tag.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import kr.mybrary.bookservice.tag.MeaningTagDtoTestData;
import kr.mybrary.bookservice.tag.MeaningTagFixture;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagFindPageServiceRequest;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import kr.mybrary.bookservice.tag.persistence.repository.MeaningTagRepository;
import kr.mybrary.bookservice.tag.persistence.repository.MyBookMeaningTagRepository;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MeaningTagServiceTest {

    @InjectMocks
    private MeaningTagService meaningTagService;

    @Mock
    private MeaningTagRepository meaningTagRepository;

    @Mock
    private MyBookMeaningTagRepository myBookMeaningTagRepository;

    @DisplayName("기존 의미 태그가 지정되어 있을 경우, 마이북에 의미 태그를 변경한다.")
    @Test
    void assignMeaningTagExistingOriginalMeaningTag() {

        // given
        MeaningTagAssignServiceRequest request = MeaningTagDtoTestData.createMeaningTagAssignServiceRequest();
        MeaningTag meaningTag = MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                .quote(request.getQuote()).build();

        MeaningTag originalMeaningTag = MeaningTagFixture.COMMON_MEANING_REGISTERED_COUNT_10_TAG.getMeaningTag();
        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTagBuilder()
                .meaningTag(originalMeaningTag)
                .build();

        int newMeaningTagRegisteredCount = meaningTag.getRegisteredCount();
        int originalMeaningTagRegisteredCount = originalMeaningTag.getRegisteredCount();

        given(meaningTagRepository.findByQuote(any())).willReturn(Optional.ofNullable(meaningTag));
        given(myBookMeaningTagRepository.findByMyBook(any())).willReturn(Optional.ofNullable(myBookMeaningTag));

        // when
        meaningTagService.assignMeaningTag(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findByQuote(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).findByMyBook(any()),
                () -> assertThat(meaningTag.getRegisteredCount()).isEqualTo(newMeaningTagRegisteredCount + 1),
                () -> assertThat(originalMeaningTag.getRegisteredCount()).isEqualTo(originalMeaningTagRegisteredCount - 1),
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

    @DisplayName("기존 의미 태그가 지정되어 있지 않는 경우, 마이북에 의미 태그를 변경한다.")
    @Test
    void assignMeaningTagInitially() {

        // given
        MeaningTagAssignServiceRequest request = MeaningTagDtoTestData.createMeaningTagAssignServiceRequest();
        MeaningTag meaningTag = MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                .quote(request.getQuote()).build();

        int newMeaningTagRegisteredCount = meaningTag.getRegisteredCount();

        given(meaningTagRepository.findByQuote(any())).willReturn(Optional.ofNullable(meaningTag));
        given(myBookMeaningTagRepository.findByMyBook(any())).willReturn(Optional.empty());

        // when
        meaningTagService.assignMeaningTag(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findByQuote(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).findByMyBook(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).save(any()),
                () -> assertThat(meaningTag.getRegisteredCount()).isEqualTo(newMeaningTagRegisteredCount + 1)
        );
    }

    @DisplayName("새로운 의미 태그의 문구를 마이북에 지정한다.")
    @Test
    void assignNewMeaningTag() {

        // given
        MeaningTagAssignServiceRequest request = MeaningTagDtoTestData.createMeaningTagAssignServiceRequest();

        MeaningTag meaningTag = MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTag();
        int newMeaningTagRegisteredCount = meaningTag.getRegisteredCount();

        given(meaningTagRepository.findByQuote(any())).willReturn(Optional.empty());
        given(myBookMeaningTagRepository.findByMyBook(any())).willReturn(Optional.empty());
        given(meaningTagRepository.save(any())).willReturn(meaningTag);

        // when
        meaningTagService.assignMeaningTag(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findByQuote(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).findByMyBook(any()),
                () -> verify(meaningTagRepository, times(1)).save(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).save(any()),
                () -> assertThat(meaningTag.getRegisteredCount()).isEqualTo(newMeaningTagRegisteredCount + 1)
        );
    }

    @DisplayName("의미 태그 문구가 \"\"인 경우에 마이북 의미 태그를 삭제한다.")
    @Test
    void deleteMyBookMeaningTag() {

        // given
        MeaningTagAssignServiceRequest request = MeaningTagDtoTestData.createMeaningTagAssignServiceRequestWithEmptyQuote();
        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag();

        given(myBookMeaningTagRepository.findByMyBook(any())).willReturn(
                Optional.of(myBookMeaningTag));

        // when
        meaningTagService.assignMeaningTag(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, never()).findByQuote(any()),
                () -> verify(myBookMeaningTagRepository, times(1)).findByMyBook(any()),
                () -> assertThat(myBookMeaningTag.getMeaningTag().getRegisteredCount()).isEqualTo(0)
        );
    }

    @DisplayName("가장 많이 등록된 수에 따라 의미 태그를 페이징 조회한다.")
    @Test
    void findPageByRegisteredCount() {

        // given
        MeaningTagFindPageServiceRequest request = MeaningTagDtoTestData.createMeaningTagFindPageServiceRequest(10);
        PageRequest pageRequest = PageRequest.of(0, request.getSize());

        List<MeaningTag> meaningTags = new ArrayList<>() {{
            IntStream.range(0, 10)
                    .map(i -> 10-i)
                    .forEach(i -> add(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                            .registeredCount(i)
                            .build()));
        }};

        given(meaningTagRepository.findAllByOrderByRegisteredCountDesc(pageRequest)).willReturn(
                new PageImpl<>(meaningTags));

        // when
        List<MeaningTagElementResponse> result = meaningTagService.findPageOrderByRegisteredCount(request);

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findAllByOrderByRegisteredCountDesc(any()),
                () -> assertThat(result.size()).isEqualTo(10)
        );
    }

    @DisplayName("모든 의미 태그를 조회한다.")
    @Test
    void findAll() {

        // given
        List<MeaningTag> meaningTags = new ArrayList<>() {{
            IntStream.range(0, 10)
                    .forEach(i -> add(MeaningTagFixture.COMMON_MEANING_TAG.getMeaningTagBuilder()
                            .quote("MEANING_TAG_" + i)
                            .registeredCount(i)
                            .build()));
        }};

        given(meaningTagRepository.findAll()).willReturn(meaningTags);

        // when
        List<MeaningTagElementResponse> result = meaningTagService.findAll();

        // then
        assertAll(
                () -> verify(meaningTagRepository, times(1)).findAll(),
                () -> assertThat(result.size()).isEqualTo(10)
        );
    }
}