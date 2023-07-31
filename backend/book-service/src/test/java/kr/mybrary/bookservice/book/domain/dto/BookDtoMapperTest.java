package kr.mybrary.bookservice.book.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import org.junit.jupiter.api.Test;

class BookDtoMapperTest {

    @Test
    void bookCreateRequestToEntity() {

        // given
        BookCreateServiceRequest bookCreateServiceRequest = BookDtoTestData.createBookCreateServiceRequest();

        // when
        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(bookCreateServiceRequest);

        // then
        assertAll(
                () -> assertThat(book.getTitle()).isEqualTo(bookCreateServiceRequest.getTitle()),
                () -> assertThat(book.getSubTitle()).isEqualTo(bookCreateServiceRequest.getSubTitle()),
                () -> assertThat(book.getDescription()).isEqualTo(bookCreateServiceRequest.getDescription()),
                () -> assertThat(book.getLink()).isEqualTo(bookCreateServiceRequest.getLink()),
                () -> assertThat(book.getIsbn10()).isEqualTo(bookCreateServiceRequest.getIsbn10()),
                () -> assertThat(book.getIsbn13()).isEqualTo(bookCreateServiceRequest.getIsbn13()),
                () -> assertThat(book.getPublisher()).isEqualTo(bookCreateServiceRequest.getPublisher()),
                () -> assertThat(book.getPublicationDate()).isEqualTo(bookCreateServiceRequest.getPublicationDate()),
                () -> assertThat(book.getPriceSales()).isEqualTo(bookCreateServiceRequest.getPriceSales()),
                () -> assertThat(book.getPriceStandard()).isEqualTo(bookCreateServiceRequest.getPriceStandard()),
                () -> assertThat(book.getThumbnailUrl()).isEqualTo(bookCreateServiceRequest.getThumbnailUrl()),
                () -> assertThat(book.getToc()).isEqualTo(bookCreateServiceRequest.getToc()),
                () -> assertThat(book.getWeight()).isEqualTo(bookCreateServiceRequest.getWeight()),
                () -> assertThat(book.getSizeDepth()).isEqualTo(bookCreateServiceRequest.getSizeDepth()),
                () -> assertThat(book.getSizeHeight()).isEqualTo(bookCreateServiceRequest.getSizeHeight()),
                () -> assertThat(book.getSizeWidth()).isEqualTo(bookCreateServiceRequest.getSizeWidth()),
                () -> assertThat(book.getAladinReviewCount()).isEqualTo(bookCreateServiceRequest.getReviewCount()),
                () -> assertThat(book.getAladinStarRating()).isEqualTo(bookCreateServiceRequest.getStarRating()),
                () -> assertThat(book.getHolderCount()).isEqualTo(0),
                () -> assertThat(book.getReadCount()).isEqualTo(0),
                () -> assertThat(book.getInterestCount()).isEqualTo(0),
                () -> assertThat(book.getStarRating()).isEqualTo(0),
                () -> assertThat(book.getReviewCount()).isEqualTo(0)
        );
    }
}