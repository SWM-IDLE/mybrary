package kr.mybrary.bookservice.review.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.review.MyBookReviewDtoTestData;
import kr.mybrary.bookservice.review.persistence.model.ReviewFromMyBookModel;
import kr.mybrary.bookservice.review.presentation.dto.response.ReviewOfMyBookGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyBookReviewDtoMapperTest {

    @DisplayName("마이북 리뷰 모델을 응답 DTO로 변환한다.")
    @Test
    void ReviewOfMyBookModelToResponse() {

        // given
        ReviewFromMyBookModel source = MyBookReviewDtoTestData.createReviewFromMyBookModel();

        // when
        ReviewOfMyBookGetResponse target = MyBookReviewDtoMapper.INSTANCE.reviewOfMyBookModelToResponse(source);

        // then
        assertAll(
                () -> assertThat(target.getId()).isEqualTo(source.getId()),
                () -> assertThat(target.getContent()).isEqualTo(source.getContent()),
                () -> assertThat(target.getStarRating()).isEqualTo(source.getStarRating()),
                () -> assertThat(target.getCreatedAt()).isEqualTo(MyBookReviewDtoMapper.toFormatMyBookReviewUI(source.getCreatedAt())),
                () -> assertThat(target.getUpdatedAt()).isEqualTo(MyBookReviewDtoMapper.toFormatMyBookReviewUI(source.getUpdatedAt()))
        );
    }

    @DisplayName("LocalDateTime을 yyyy.MM.dd에 맞는 형식으로 변환한다.")
    @Test
    void toFormatMyBookReviewUI() {

        // given
        String expected = "2021.01.01";

        // when
        LocalDateTime source = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        String target = MyBookReviewDtoMapper.toFormatMyBookReviewUI(source);

        // then
        assertThat(target).isEqualTo(expected);
    }
}