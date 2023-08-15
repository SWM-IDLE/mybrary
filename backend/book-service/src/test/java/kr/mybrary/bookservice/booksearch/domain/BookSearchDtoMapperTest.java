package kr.mybrary.bookservice.booksearch.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.mybrary.bookservice.booksearch.BookSearchDtoTestData;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponseElement;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse.Item;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse.Document;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse.Author;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse.Translator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookSearchDtoMapperTest {

    private final static String ISBN_10_AND_13 = "8980782977 9788980782970";
    private final static String ISBN_10 = "8980782977";
    private final static String ISBN_13 = "9788980782970";

    private final static String NOT_PROVIDED_PHRASES = "제공되지 않습니다.";

    @DisplayName("카카오 도서 검색 응답 값을 BookSearchResultServiceResponse 로 매핑한다.")
    @Test
    void kakaoSearchResponseToDto() {

        KakaoBookSearchResponse kakaoBookSearchResponse = BookSearchDtoTestData.createKakaoBookSearchResponse();
        Document response = kakaoBookSearchResponse.getDocuments().get(0);

        BookSearchResultResponseElement dto = BookSearchDtoMapper.INSTANCE.kakaoSearchResponseToServiceResponse(
                response);

        assertAll(
                () -> assertThat(dto.getTitle()).isEqualTo(response.getTitle()),
                () -> assertThat(dto.getDescription()).isEqualTo(response.getContents()),
                () -> assertThat(dto.getAuthor()).isEqualTo(String.join(",", response.getAuthors())),
                () -> assertThat(dto.getIsbn13()).isEqualTo(ISBN_13),
                () -> assertThat(dto.getThumbnailUrl()).isEqualTo(response.getThumbnail()),
                () -> assertThat(dto.getPublicationDate()).isEqualTo(response.getDatetime().split("T")[0]),
                () -> assertThat(dto.getStarRating()).isEqualTo(0.0)
        );
    }

    @DisplayName("알라딘 도서 검색 응답 값을 BookSearchResultServiceResponse 로 매핑한다.")
    @Test
    void aladinSearchResponseToDto() {

        AladinBookSearchResponse aladinBookSearchResponse = BookSearchDtoTestData.createAladinBookSearchResponse();

        Item response = aladinBookSearchResponse.getItem().get(0);
        BookSearchResultResponseElement dto = BookSearchDtoMapper.INSTANCE.aladinSearchResponseToServiceResponse(
                response);

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

    @DisplayName("카카오 도서 검색 응답 값을 BookSearchDetailResponse 로 매핑한다.")
    @Test
    void kakaoSearchResponseToDetailResponse() {

        KakaoBookSearchResponse kakaoBookSearchResponse = BookSearchDtoTestData.createKakaoBookSearchResponse();
        Document response = kakaoBookSearchResponse.getDocuments().get(0);

        BookSearchDetailResponse dto = BookSearchDtoMapper.INSTANCE.kakaoSearchResponseToDetailResponse(response);

        assertAll(
                () -> assertThat(dto.getTitle()).isEqualTo(response.getTitle()),
                () -> assertThat(dto.getSubTitle()).isEqualTo(NOT_PROVIDED_PHRASES),
                () -> assertThat(dto.getAuthor()).isEqualTo(String.join(",", response.getAuthors())),
                () -> assertThat(dto.getThumbnail()).isEqualTo(response.getThumbnail()),
                () -> assertThat(dto.getDescription()).isEqualTo(response.getContents()),
                () -> assertThat(dto.getIsbn10()).isEqualTo(ISBN_10),
                () -> assertThat(dto.getIsbn13()).isEqualTo(ISBN_13),
                () -> assertThat(dto.getPublicationDate()).isEqualTo(response.getDatetime()),
                () -> assertThat(dto.getPublisher()).isEqualTo(response.getPublisher()),
                () -> assertThat(dto.getPriceSales()).isEqualTo(response.getSale_price()),
                () -> assertThat(dto.getAuthors().size()).isEqualTo(2),
                () -> assertThat(dto.getTranslators().size()).isEqualTo(2),
                () -> assertThat(dto.getToc()).isEqualTo(NOT_PROVIDED_PHRASES),
                () -> assertThat(dto.getCategory()).isEqualTo(NOT_PROVIDED_PHRASES),
                () -> assertThat(dto.getCategoryId()).isEqualTo(0),
                () -> assertThat(dto.getStarRating()).isEqualTo(0.0),
                () -> assertThat(dto.getHolderCount()).isEqualTo(0),
                () -> assertThat(dto.getReadCount()).isEqualTo(0),
                () -> assertThat(dto.getInterestCount()).isEqualTo(0)
        );
    }


    @DisplayName("ISBN10과 ISBN13이 모두 존재할 경우, ISBN10과 ISBN13으로 파싱해서 ISBN을 가져온다.")
    @Test
    void getISBNWhenHasISBN10And13() {

        // given, when
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

        // given, when
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

        // given, when
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_13);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_13);

        // then
        assertAll(
                () -> assertEquals("", isbn10),
                () -> assertEquals(ISBN_13, isbn13)
        );
    }

    @DisplayName("Kakao API Response의 Author Names를 BookSearchDetailResponse의 authors 매핑한다.")
    @Test
    void mappingAuthorNames() {

        // given
        List<String> authorNameList = List.of("저자1", "저자2", "저자3");

        // when
        List<Author> authors = BookSearchDtoMapper.mappingAuthorNames(authorNameList);

        // then
        assertAll(
                () -> {
                    assertThat(authors).hasSize(3);
                    assertThat(authors.get(0).getName()).isEqualTo("저자1");
                    assertThat(authors.get(0).getAuthorId()).isEqualTo(0);
                    assertThat(authors.get(1).getName()).isEqualTo("저자2");
                    assertThat(authors.get(1).getAuthorId()).isEqualTo(0);
                    assertThat(authors.get(2).getName()).isEqualTo("저자3");
                    assertThat(authors.get(2).getAuthorId()).isEqualTo(0);
                }
        );
    }

    @DisplayName("Kakao API Response의 Translator Names를 BookSearchDetailResponse의 translators 매핑한다.")
    @Test
    void mappingTranslatorNames() {

        // given
        List<String> translatorNameList = List.of("번역가1", "번역가2", "번역가3");

        // when
        List<Translator> translators = BookSearchDtoMapper.mappingTranslatorNames(translatorNameList);

        // then
        assertAll(
                () -> {
                    assertThat(translators).hasSize(3);
                    assertThat(translators.get(0).getName()).isEqualTo("번역가1");
                    assertThat(translators.get(0).getTranslatorId()).isEqualTo(0);
                    assertThat(translators.get(1).getName()).isEqualTo("번역가2");
                    assertThat(translators.get(1).getTranslatorId()).isEqualTo(0);
                    assertThat(translators.get(2).getName()).isEqualTo("번역가3");
                    assertThat(translators.get(2).getTranslatorId()).isEqualTo(0);
                }
        );
    }

    @DisplayName("알라딘 API Response의 Translator (Id, Name)를 BookSearchDetailResponse의 translators 매핑한다.")
    @Test
    void mappingTranslatorNamesAndIds() {

        // given
        List<AladinBookSearchDetailResponse.Author> authorList = List.of(
                AladinBookSearchDetailResponse.Author.builder().authorId(1).authorName("번역가1").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(2).authorName("번역가2").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(3).authorName("번역가3").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(4).authorName("지은이1").authorType("author").build()
        );

        // when
        List<Translator> translators = BookSearchDtoMapper.mappingTranslatorNamesAndIds(authorList);

        // then
        assertAll(
                () -> assertThat(translators).hasSize(3),
                () -> assertThat(translators).extracting(
                        Translator::getTranslatorId,
                        Translator::getName
                ).containsExactly(
                        tuple(1, "번역가1"),
                        tuple(2, "번역가2"),
                        tuple(3, "번역가3")
                )
        );
    }

    @DisplayName("알라딘 API Response의 Author (Id, Name)를 BookSearchDetailResponse 의 authors 매핑한다.")
    @Test
    void mappingAuthorNamesAndIds() {

        // given
        List<AladinBookSearchDetailResponse.Author> authorList = List.of(
                AladinBookSearchDetailResponse.Author.builder().authorId(1).authorName("번역가1").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(2).authorName("번역가2").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(3).authorName("번역가3").authorType("translator").build(),
                AladinBookSearchDetailResponse.Author.builder().authorId(4).authorName("지은이1").authorType("author").build()
        );

        // when
        List<Author> authors = BookSearchDtoMapper.mappingAuthorNamesAndIds(authorList);

        // then
        assertAll(
                () -> assertThat(authors).hasSize(1),
                () -> assertThat(authors).extracting(
                        Author::getAuthorId,
                        Author::getName
                ).containsExactly(
                        tuple(4, "지은이1")
                )
        );
    }

    @DisplayName("알라딘 도서 검색 응답 값을 BookSearchDetailResponse 로 매핑한다.")
    @Test
    void aladinSearchResponseToDetailResponse() {

        AladinBookSearchDetailResponse.Item response = BookSearchDtoTestData.createAladinBookSearchDetailResponseItem();

        BookSearchDetailResponse dto = BookSearchDtoMapper.INSTANCE.aladinSearchResponseToDetailResponse(
                response);

        assertAll(
                () -> assertThat(dto.getTitle()).isEqualTo(response.getTitle().replace(" - " + response.getSubInfo().getSubTitle(), "")),
                () -> assertThat(dto.getSubTitle()).isEqualTo(response.getSubInfo().getSubTitle()),
                () -> assertThat(dto.getAuthor()).isEqualTo(response.getAuthor()),
                () -> assertThat(dto.getLink()).isEqualTo(response.getLink()),
                () -> assertThat(dto.getThumbnail()).isEqualTo(response.getCover()),
                () -> assertThat(dto.getDescription()).isEqualTo(response.getFullDescription()),
                () -> assertThat(dto.getIsbn10()).isEqualTo(response.getIsbn()),
                () -> assertThat(dto.getIsbn13()).isEqualTo(response.getIsbn13()),
                () -> assertThat(dto.getPublicationDate()).isEqualTo(response.getPubDate()),
                () -> assertThat(dto.getPublisher()).isEqualTo(response.getPublisher()),
                () -> assertThat(dto.getPriceSales()).isEqualTo(response.getPriceSales()),
                () -> assertThat(dto.getAuthors().size()).isEqualTo(1),
                () -> assertThat(dto.getTranslators().size()).isEqualTo(1),
                () -> assertThat(dto.getToc()).isEqualTo(response.getSubInfo().getToc()),
                () -> assertThat(dto.getCategory()).isEqualTo(response.getCategoryName()),
                () -> assertThat(dto.getCategoryId()).isEqualTo(response.getCategoryId()),
                () -> assertThat(dto.getReviewCount()).isEqualTo(response.getSubInfo().getRatingInfo().getMyReviewCount()),
                () -> assertThat(dto.getStarRating()).isEqualTo(response.getCustomerReviewRank() / 2.0),
                () -> assertThat(dto.getWeight()).isEqualTo(response.getSubInfo().getPacking().getWeight()),
                () -> assertThat(dto.getSizeDepth()).isEqualTo(response.getSubInfo().getPacking().getSizeDepth()),
                () -> assertThat(dto.getSizeHeight()).isEqualTo(response.getSubInfo().getPacking().getSizeHeight()),
                () -> assertThat(dto.getSizeWidth()).isEqualTo(response.getSubInfo().getPacking().getSizeWidth()),
                () -> assertThat(dto.getPriceStandard()).isEqualTo(response.getPriceStandard()),
                () -> assertThat(dto.getPriceSales()).isEqualTo(response.getPriceSales()),
                () -> assertThat(dto.getHolderCount()).isEqualTo(0),
                () -> assertThat(dto.getReadCount()).isEqualTo(0),
                () -> assertThat(dto.getInterestCount()).isEqualTo(0)
        );
    }
}