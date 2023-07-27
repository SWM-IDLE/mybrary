package kr.mybrary.bookservice.booksearch.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse.Item;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookSearchDtoMapperTest {

    private final static String ISBN_10_AND_13 = "8980782977 9788980782970";
    private final static String ISBN_10 = "8980782977";
    private final static String ISBN_13 = "9788980782970";

    @DisplayName("카카오 도서 검색 응답 값을 BookSearchResultServiceResponse 로 매핑한다.")
    @Test
    void kakaoSearchResponseToDto() {

        KakaoBookSearchResponse kakaoBookSearchResponse = BookSearchDtoTestData.createKakaoBookSearchResponse();
        Document response = kakaoBookSearchResponse.getDocuments().get(0);

        BookSearchResultServiceResponse dto = BookSearchDtoMapper.INSTANCE.kakaoSearchResponseToServiceResponse(response);

        assertAll(
                () -> assertThat(dto.getTitle()).isEqualTo(response.getTitle()),
                () -> assertThat(dto.getDescription()).isEqualTo(response.getContents()),
                () -> assertThat(dto.getAuthor()).isEqualTo(String.join(",", response.getAuthors())),
                () -> assertThat(dto.getIsbn13()).isEqualTo(response.getIsbn()),
                () -> assertThat(dto.getThumbnailUrl()).isEqualTo(response.getThumbnail()),
                () -> assertThat(dto.getPublicationDate()).isEqualTo(response.getDatetime()),
                () -> assertThat(dto.getStarRating()).isEqualTo(0.0)
        );
    }

    @DisplayName("알라딘 도서 검색 응답 값을 BookSearchResultServiceResponse로 매핑한다.")
    @Test
    void aladinSearchResponseToDto() {

        AladinBookSearchResponse aladinBookSearchResponse = BookSearchDtoTestData.createAladinBookSearchResponse();

        Item response = aladinBookSearchResponse.getItem().get(0);
        BookSearchResultServiceResponse dto = BookSearchDtoMapper.INSTANCE.aladinSearchResponseToServiceResponse(response);

        assertAll(
                () -> assertThat(dto.getTitle()).isEqualTo(response.getTitle()),
                () -> assertThat(dto.getDescription()).isEqualTo(response.getDescription()),
                () -> assertThat(dto.getAuthor()).isEqualTo(response.getAuthor()),
                () -> assertThat(dto.getIsbn13()).isEqualTo(response.getIsbn13()),
                () -> assertThat(dto.getThumbnailUrl()).isEqualTo(response.getCover()),
                () -> assertThat(dto.getPublicationDate()).isEqualTo(response.getPubDate()),
                () -> assertThat(dto.getStarRating()).isEqualTo(response.getCustomerReviewRank() / 2.0)
        );
    }

    @DisplayName("ISBN10과 ISBN13이 모두 존재할 경우, ISBN10과 ISBN13으로 파싱해서 ISBN을 가져온다.")
    @Test
    void getISBNWhenHasISBN10And13() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_10_AND_13);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_10_AND_13);

        // then
        assertAll(
                () -> assertEquals(ISBN_10, isbn10),
                () -> assertEquals(ISBN_13, isbn13)
        );
    }

    @DisplayName("ISBN10만 존재할 경우, ISBN10을 가져온다.")
    @Test
    void getISBNWhenHasISBN10() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_10);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_10);

        // then
        assertAll(
                () -> assertEquals(ISBN_10, isbn10),
                () -> assertEquals("", isbn13)
        );
    }

    @DisplayName("ISBN13만 존재할 경우, ISBN13을 가져온다.")
    @Test
    void getISBNWhenHasISBN13() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_13);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_13);

        // then
        assertAll(
                () -> assertEquals("", isbn10),
                () -> assertEquals(ISBN_13, isbn13)
        );
    }
}