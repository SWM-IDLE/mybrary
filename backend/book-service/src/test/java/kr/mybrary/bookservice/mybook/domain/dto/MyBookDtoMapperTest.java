package kr.mybrary.bookservice.mybook.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.MybookDtoTestData;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.model.MyBookListDisplayElementModel;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.tag.MyBookMeaningTagFixture;
import kr.mybrary.bookservice.tag.persistence.MyBookMeaningTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyBookDtoMapperTest {

    @DisplayName("MyBook 엔티티를 MyBookElementResponse로 매핑한다.")
    @Test
    void entityToMyBookElementResponse() {

        // given
        MyBookListDisplayElementModel source = MybookDtoTestData.createMyBookListDisplayElementModelBuilder().build();

        // when
        MyBookElementResponse target = MyBookDtoMapper.INSTANCE.modelToMyBookElementResponse(source);

        // then
        assertAll(
                () -> assertThat(target.getId()).isEqualTo(source.getMyBookId()),
                () -> assertThat(target.getStartDateOfPossession()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(source.getStartDateOfPossession())),
                () -> assertThat(target.getReadStatus()).isEqualTo(source.getReadStatus()),
                () -> assertThat(target.isExchangeable()).isEqualTo(source.isExchangeable()),
                () -> assertThat(target.isShareable()).isEqualTo(source.isShareable()),
                () -> assertThat(target.isShowable()).isEqualTo(source.isShowable()),
                () -> assertThat(target.getBook().getId()).isEqualTo(source.getBookId()),
                () -> assertThat(target.getBook().getTitle()).isEqualTo(source.getTitle()),
                () -> assertThat(target.getBook().getDescription()).isEqualTo(source.getDescription()),
                () -> assertThat(target.getBook().getThumbnailUrl()).isEqualTo(source.getThumbnailUrl()),
                () -> assertThat(target.getBook().getStarRating()).isEqualTo(source.getStarRating()),
                () -> assertThat(target.getBook().getPublicationDate()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(source.getPublicationDate())),
                () -> assertThat(target.getBook().getAuthors()).isEqualTo("저자_1, 저자_2")
        );
    }

    @DisplayName("MyBook 엔티티를 MyBookDeatail로 매핑한다. (MyBookMeaningTag가 존재하는 경우)")
    @Test
    void entityToMyBookDetailResponse() {

        // given
        MyBookMeaningTag myBookMeaningTag = MyBookMeaningTagFixture.COMMON_MY_BOOK_MEANING_TAG.getMyBookMeaningTag();

        MyBook myBook = MyBookFixture.MYBOOK_WITH_REVIEW.getMyBookBuilder()
                .myBookMeaningTag(myBookMeaningTag)
                .build();

        // when
        MyBookDetailResponse myBookDetailResponse = MyBookDtoMapper.INSTANCE.entityToMyBookDetailResponse(myBook);

        // then
        assertAll(
                () -> assertThat(myBookDetailResponse.getId()).isEqualTo(myBook.getId()),
                () -> assertThat(myBookDetailResponse.getStartDateOfPossession()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(myBook.getStartDateOfPossession())),
                () -> assertThat(myBookDetailResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookDetailResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookDetailResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookDetailResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookDetailResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookDetailResponse.getBook().getIsbn13()).isEqualTo(myBook.getBook().getIsbn13()),
                () -> assertThat(myBookDetailResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookDetailResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookDetailResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookDetailResponse.getBook().getStarRating()).isEqualTo(0.0),
                () -> assertThat(myBookDetailResponse.getBook().getAuthors()).isEqualTo(myBook.getBook().getBookAuthors().stream()
                        .map(bookAuthor -> bookAuthor.getAuthor().getName())
                        .toList()),
                () -> assertThat(myBookDetailResponse.getBook().getTranslators()).isEqualTo(myBook.getBook().getBookTranslators().stream()
                        .map(bookTranslator -> bookTranslator.getTranslator().getName())
                        .toList()),
                () -> assertThat(myBookDetailResponse.getMeaningTag().getQuote()).isEqualTo(myBookMeaningTag.getMeaningTag().getQuote()),
                () -> assertThat(myBookDetailResponse.getMeaningTag().getColorCode()).isEqualTo(myBookMeaningTag.getMeaningTagColor())
        );
    }

    @DisplayName("MyBook 엔티티를 MyBookDeatail로 매핑시, MyBookMeaningTag가 존재하지 않을 경우, \"\"로 매핑한다.")
    @Test
    void entityToMyBookDetailResponseWithOutMyBookMeaningTag() {

        // given
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBookBuilder()
                .build();

        // when
        MyBookDetailResponse myBookDetailResponse = MyBookDtoMapper.INSTANCE.entityToMyBookDetailResponse(myBook);

        // then
        assertAll(
                () -> assertThat(myBookDetailResponse.getId()).isEqualTo(myBook.getId()),
                () -> assertThat(myBookDetailResponse.getStartDateOfPossession()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(myBook.getStartDateOfPossession())),
                () -> assertThat(myBookDetailResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookDetailResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookDetailResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookDetailResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookDetailResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookDetailResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookDetailResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookDetailResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookDetailResponse.getBook().getStarRating()).isEqualTo(0.0),
                () -> assertThat(myBookDetailResponse.getBook().getAuthors()).isEqualTo(myBook.getBook().getBookAuthors().stream()
                        .map(bookAuthor -> bookAuthor.getAuthor().getName())
                        .toList()),
                () -> assertThat(myBookDetailResponse.getBook().getTranslators()).isEqualTo(myBook.getBook().getBookTranslators().stream()
                        .map(bookTranslator -> bookTranslator.getTranslator().getName())
                        .toList()),
                () -> assertThat(myBookDetailResponse.getMeaningTag().getQuote()).isEqualTo(""),
                () -> assertThat(myBookDetailResponse.getMeaningTag().getColorCode()).isEqualTo("")
        );
    }
}