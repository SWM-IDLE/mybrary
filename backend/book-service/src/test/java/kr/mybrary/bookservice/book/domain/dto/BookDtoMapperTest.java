package kr.mybrary.bookservice.book.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse.Author;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse.Translator;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookDtoMapperTest {

    @DisplayName("BookCreateServiceRequest를 Book Entity로 매핑한다.")
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

    @DisplayName("Book 엔티티의 BookAuthor의 저자를 BookDetailServiceResponse.Author로 매핑한다.")
    @Test
    void mappingAuthors() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();

        // when
        List<Author> authors = BookDtoMapper.mappingAuthors(book.getBookAuthors());

        // then
        assertAll(
                () -> assertThat(authors.get(0).getName()).isEqualTo(
                        book.getBookAuthors().get(0).getAuthor().getName()),
                () -> assertThat(authors.get(0).getAuthorId()).isEqualTo(
                        book.getBookAuthors().get(0).getAuthor().getAid()),
                () -> assertThat(authors.get(1).getName()).isEqualTo(
                        book.getBookAuthors().get(1).getAuthor().getName()),
                () -> assertThat(authors.get(1).getAuthorId()).isEqualTo(
                        book.getBookAuthors().get(1).getAuthor().getAid())
        );
    }

    @DisplayName("Book 엔티티의 BookTranslator의 번역가를 BookDetailServiceResponse.Translator로 매핑한다.")
    @Test
    void mappingTranslators() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();

        // when
        List<Translator> translators = BookDtoMapper.mappingTranslators(book.getBookTranslators());

        // then
        assertAll(
                () -> assertThat(translators.get(0).getName()).isEqualTo(
                        book.getBookTranslators().get(0).getTranslator().getName()),
                () -> assertThat(translators.get(0).getTranslatorId()).isEqualTo(
                        book.getBookTranslators().get(0).getTranslator().getTid()),
                () -> assertThat(translators.get(1).getName()).isEqualTo(
                        book.getBookTranslators().get(1).getTranslator().getName()),
                () -> assertThat(translators.get(1).getTranslatorId()).isEqualTo(
                        book.getBookTranslators().get(1).getTranslator().getTid())
        );
    }

    @DisplayName("Book 엔티티를 BookDetailServiceResponse로 매핑한다.")
    @Test
    void bookToDetailServiceResponse() {

        // given
        Book book = BookFixture.COMMON_BOOK.getBook();

        // when
        BookDetailServiceResponse bookDetailServiceResponse = BookDtoMapper.INSTANCE.bookToDetailServiceResponse(book);

        // then
        assertAll(
                () -> assertThat(bookDetailServiceResponse.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(bookDetailServiceResponse.getStarRating()).isEqualTo(book.getAladinStarRating()),
                () -> assertThat(bookDetailServiceResponse.getReviewCount()).isEqualTo(book.getAladinReviewCount()),
                () -> assertThat(bookDetailServiceResponse.getAuthors().size()).isEqualTo(book.getBookAuthors().size()),
                () -> assertThat(bookDetailServiceResponse.getTranslators().size()).isEqualTo(
                        book.getBookTranslators().size()),
                () -> assertThat(bookDetailServiceResponse.getIsbn10()).isEqualTo(book.getIsbn10()),
                () -> assertThat(bookDetailServiceResponse.getIsbn13()).isEqualTo(book.getIsbn13())
        );
    }

    @DisplayName("BookSearchDetailResponse를 BookDetailServiceResponse로 매핑한다.")
    @Test
    void bookSearchDetailToDetailServiceResponse() {

        // given
        BookSearchDetailResponse source = BookSearchDtoTestData.createBookSearchDetailResponse();

        // when
        BookDetailServiceResponse target = BookDtoMapper.INSTANCE.bookSearchDetailToDetailServiceResponse(source);

        // then
        assertAll(
                () -> assertThat(target.getIsbn13()).isEqualTo(source.getIsbn13()),
                () -> assertThat(target.getIsbn10()).isEqualTo(source.getIsbn10()),
                () -> assertThat(target.getTitle()).isEqualTo(source.getTitle()),
                () -> assertThat(target.getSubTitle()).isEqualTo(source.getSubTitle()),
                () -> assertThat(target.getDescription()).isEqualTo(source.getDescription()),
                () -> assertThat(target.getAuthors().size()).isEqualTo(source.getAuthors().size()),
                () -> assertThat(target.getTranslators().size()).isEqualTo(source.getTranslators().size()),
                () -> assertThat(target.getPublisher()).isEqualTo(source.getPublisher()),
                () -> assertThat(target.getPublicationDate()).isEqualTo(source.getPublicationDate()),
                () -> assertThat(target.getPriceSales()).isEqualTo(source.getPriceSales()),
                () -> assertThat(target.getPriceStandard()).isEqualTo(source.getPriceStandard()),
                () -> assertThat(target.getLink()).isEqualTo(source.getLink()),
                () -> assertThat(target.getToc()).isEqualTo(source.getToc()),
                () -> assertThat(target.getLink()).isEqualTo(source.getLink()),
                () -> assertThat(target.getWeight()).isEqualTo(source.getWeight()),
                () -> assertThat(target.getSizeWidth()).isEqualTo(source.getSizeWidth()),
                () -> assertThat(target.getSizeHeight()).isEqualTo(source.getSizeHeight()),
                () -> assertThat(target.getSizeDepth()).isEqualTo(source.getSizeDepth()),
                () -> assertThat(target.getStarRating()).isEqualTo(source.getStarRating()),
                () -> assertThat(target.getReviewCount()).isEqualTo(source.getReviewCount()),
                () -> assertThat(target.getAuthors().size()).isEqualTo(source.getAuthors().size()),
                () -> assertThat(target.getTranslators().size()).isEqualTo(source.getTranslators().size())
        );
    }
}