package kr.mybrary.bookservice.book.domain.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.BookDtoTestData;
import kr.mybrary.bookservice.book.BookFixture;
import kr.mybrary.bookservice.book.BookInterestFixture;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse.Author;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse.Translator;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
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
        BookDetailResponse bookDetailResponse = BookDtoMapper.INSTANCE.bookToDetailServiceResponse(book);

        // then
        assertAll(
                () -> assertThat(bookDetailResponse.getTitle()).isEqualTo(book.getTitle()),
                () -> assertThat(bookDetailResponse.getThumbnail()).isEqualTo(book.getThumbnailUrl()),
                () -> assertThat(bookDetailResponse.getStarRating()).isEqualTo(getAverageStarRating(book)),
                () -> assertThat(bookDetailResponse.getReviewCount()).isEqualTo(book.getReviewCount()),
                () -> assertThat(bookDetailResponse.getAladinStarRating()).isEqualTo(book.getAladinStarRating()),
                () -> assertThat(bookDetailResponse.getAladinReviewCount()).isEqualTo(book.getAladinReviewCount()),
                () -> assertThat(bookDetailResponse.getAuthors().size()).isEqualTo(book.getBookAuthors().size()),
                () -> assertThat(bookDetailResponse.getTranslators().size()).isEqualTo(book.getBookTranslators().size()),
                () -> assertThat(bookDetailResponse.getIsbn10()).isEqualTo(book.getIsbn10()),
                () -> assertThat(bookDetailResponse.getIsbn13()).isEqualTo(book.getIsbn13()),
                () -> assertThat(bookDetailResponse.getCategory()).isEqualTo(book.getBookCategory().getName()),
                () -> assertThat(bookDetailResponse.getCategoryId()).isEqualTo(book.getBookCategory().getCid()),
                () -> assertThat(bookDetailResponse.getPublicationDate()).isEqualTo(BookDtoMapper.localDateTimeToString(book.getPublicationDate()))
        );
    }

    @DisplayName("BookSearchDetailResponse를 BookDetailServiceResponse로 매핑한다.")
    @Test
    void bookSearchDetailToDetailServiceResponse() {

        // given
        BookSearchDetailResponse source = BookSearchDtoTestData.createBookSearchDetailResponse();

        // when
        BookDetailResponse target = BookDtoMapper.INSTANCE.bookSearchDetailToDetailServiceResponse(source);

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
                () -> assertThat(target.getAladinStarRating()).isEqualTo(source.getStarRating()),
                () -> assertThat(target.getAladinReviewCount()).isEqualTo(source.getReviewCount()),
                () -> assertThat(target.getStarRating()).isEqualTo(0),
                () -> assertThat(target.getReviewCount()).isEqualTo(0),
                () -> assertThat(target.getAuthors().size()).isEqualTo(source.getAuthors().size()),
                () -> assertThat(target.getTranslators().size()).isEqualTo(source.getTranslators().size())
        );
    }

    @DisplayName("yyyy-MM-dd 형식의 날짜를 LocalDateTime 형식으로 변환한다.")
    @Test
    void stringToLocalDateTime() {

        // given
        String source = "2021-01-01";

        // when
        LocalDateTime target = BookDtoMapper.stringToLocalDateTime(source);

        // then
        assertAll(
                () -> assertThat(target.getYear()).isEqualTo(2021),
                () -> assertThat(target.getMonthValue()).isEqualTo(1),
                () -> assertThat(target.getDayOfMonth()).isEqualTo(1)
        );
    }

    @DisplayName("LocalDateTime 형식을 yyyy-MM-dd 형식의 String으로 변환한다.")
    @Test
    void localDateTimeToString() {

        // given
        LocalDateTime localDateTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        // when
        String target = BookDtoMapper.localDateTimeToString(localDateTime);

        // then
        assertAll(
                () -> assertThat(target).isEqualTo("2021-01-01")
        );
    }

    @DisplayName("BookSearchDetailResponse를 BookCreateServiceRequest로 매핑한다.")
    @Test
    void bookSearchDetailToBookCreateServiceRequest() {

        // given
        BookSearchDetailResponse source = BookSearchDtoTestData.createBookSearchDetailResponse();

        // when
        BookCreateServiceRequest target = BookDtoMapper.INSTANCE.bookSearchDetailToBookCreateServiceRequest(source);

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
                () -> assertThat(target.getPublicationDate()).isEqualTo(
                        BookDtoMapper.stringToLocalDateTime(source.getPublicationDate())),
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
                () -> assertThat(target.getTranslators().size()).isEqualTo(source.getTranslators().size()),
                () -> assertThat(target.getThumbnailUrl()).isEqualTo(source.getThumbnail())
        );
    }

    @DisplayName("BookInterest를 BookInterestElementResponse로 매핑한다.")
    @Test
    void bookInterestToBookInterestElementResponse() {

        // given
        BookInterest source = BookInterestFixture.COMMON_BOOK_INTEREST.getBookInterest();

        // when
        BookInterestElementResponse target = BookDtoMapper.INSTANCE.bookInterestToBookInterestElementResponse(source);

        // then
        assertAll(
                () -> assertThat(target.getId()).isEqualTo(source.getBook().getId()),
                () -> assertThat(target.getTitle()).isEqualTo(source.getBook().getTitle()),
                () -> assertThat(target.getIsbn13()).isEqualTo(source.getBook().getIsbn13()),
                () -> assertThat(target.getThumbnailUrl()).isEqualTo(source.getBook().getThumbnailUrl()),
                () -> assertThat(target.getAuthor()).isEqualTo(source.getBook().getAuthor())
        );
    }

    private double getAverageStarRating(Book book) {
        if (book.getReviewCount() == 0) {
            return 0.0;
        }
        double averageRating = (double) book.getStarRating() / book.getReviewCount();
        return Math.round(averageRating * 10.0) / 10.0;
    }
}