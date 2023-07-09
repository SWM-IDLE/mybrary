package kr.mybrary.bookservice.mybook.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.mybook.MybookTestData;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyBookDtoMapperTest {

    @DisplayName("MyBook 엔티티를 MyBookCreateRequest 매핑한다.")
    @Test
    void entityToMyBookElementResponse() {

        MyBook myBook = MybookTestData.createMyBook();

        MyBookElementResponse myBookElementResponse = MyBookDtoMapper.INSTANCE.entityToMyBookElementResponse(myBook);

        assertAll(
                () -> assertThat(myBookElementResponse.getId()).isEqualTo(myBook.getId()),
                () -> assertThat(myBookElementResponse.getStartDateOfPossession()).isEqualTo(myBook.getStartDateOfPossession()),
                () -> assertThat(myBookElementResponse.getReadStatus()).isEqualTo(myBook.getReadStatus()),
                () -> assertThat(myBookElementResponse.isExchangeable()).isEqualTo(myBook.isExchangeable()),
                () -> assertThat(myBookElementResponse.isShareable()).isEqualTo(myBook.isShareable()),
                () -> assertThat(myBookElementResponse.isShowable()).isEqualTo(myBook.isShowable()),
                () -> assertThat(myBookElementResponse.getBook().getId()).isEqualTo(myBook.getBook().getId()),
                () -> assertThat(myBookElementResponse.getBook().getTitle()).isEqualTo(myBook.getBook().getTitle()),
                () -> assertThat(myBookElementResponse.getBook().getDescription()).isEqualTo(myBook.getBook().getDescription()),
                () -> assertThat(myBookElementResponse.getBook().getThumbnailUrl()).isEqualTo(myBook.getBook().getThumbnailUrl()),
                () -> assertThat(myBookElementResponse.getBook().getStars()).isEqualTo(0.0)
        );
    }
}