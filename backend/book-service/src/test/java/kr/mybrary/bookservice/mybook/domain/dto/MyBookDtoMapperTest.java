package kr.mybrary.bookservice.mybook.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.global.util.DateUtils;
import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
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
        MyBook myBook = MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook();

        // when
        MyBookElementResponse myBookElementResponse = MyBookDtoMapper.INSTANCE.entityToMyBookElementResponse(myBook);

        // then
        assertAll(
                () -> assertThat(myBookElementResponse.getId()).isEqualTo(myBook.getId()),
                () -> assertThat(myBookElementResponse.getStartDateOfPossession()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(myBook.getStartDateOfPossession())),
                () -> assertThat(myBookElementResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookElementResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookElementResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookElementResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookElementResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookElementResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookElementResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookElementResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookElementResponse.getBook().getStars()).isEqualTo(myBook.getBook().getStarRating()),
                () -> assertThat(myBookElementResponse.getBook().getPublicationDate()).isEqualTo(DateUtils.toDotFormatYYYYMMDD(myBook.getBook().getPublicationDate()))
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
                () -> assertThat(myBookDetailResponse.getStartDateOfPossession()).isEqualTo(myBook.getStartDateOfPossession()),
                () -> assertThat(myBookDetailResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookDetailResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookDetailResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookDetailResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookDetailResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookDetailResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookDetailResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookDetailResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookDetailResponse.getBook().getStars()).isEqualTo(0.0),
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
                () -> assertThat(myBookDetailResponse.getStartDateOfPossession()).isEqualTo(myBook.getStartDateOfPossession()),
                () -> assertThat(myBookDetailResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookDetailResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookDetailResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookDetailResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookDetailResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookDetailResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookDetailResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookDetailResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookDetailResponse.getBook().getStars()).isEqualTo(0.0),
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