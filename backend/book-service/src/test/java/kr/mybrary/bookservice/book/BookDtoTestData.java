package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestStatusServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookMyInterestFindServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse;
import kr.mybrary.bookservice.book.persistence.BookOrderType;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestStatusResponse;

public class BookDtoTestData {

    public static BookCreateRequest createBookCreateRequest() {
        return BookCreateRequest.builder()
                .title("test_title")
                .subTitle("test_subTitle")
                .author("test_author")
                .thumbnailUrl("test_thumbnailUrl")
                .link("test_link")
                .isbn10("1111111111")
                .isbn13("1111111111111")
                .pages(100)
                .publisher("test_publisher")
                .publicationDate(LocalDateTime.now())
                .description("test_description")
                .toc("test_toc")
                .weight(100)
                .sizeDepth(100)
                .sizeHeight(100)
                .sizeWidth(100)
                .priceSales(10000)
                .priceStandard(10000)
                .starRating(4.5)
                .reviewCount(100)
                .category("test_category")
                .categoryId(9)
                .translators(List.of(
                        BookCreateServiceRequest.Translator.builder()
                                .translatorId(10)
                                .name("test_translator1")
                                .build(),
                        BookCreateServiceRequest.Translator.builder()
                                .translatorId(11)
                                .name("test_translator2")
                                .build()
                ))
                .authors(List.of(
                        BookCreateServiceRequest.Author.builder()
                                .authorId(12)
                                .name("test_author1")
                                .build(),
                        BookCreateServiceRequest.Author.builder()
                                .authorId(13)
                                .name("test_author2")
                                .build()
                ))
                .build();
    }

    public static BookCreateServiceRequest createBookCreateServiceRequest() {
        return BookCreateServiceRequest.builder()
                .title("test_title")
                .subTitle("test_subTitle")
                .author("test_author")
                .thumbnailUrl("test_thumbnailUrl")
                .link("test_link")
                .isbn10("1111111111")
                .isbn13("1111111111111")
                .pages(100)
                .publisher("test_publisher")
                .publicationDate(LocalDateTime.now())
                .description("test_description")
                .toc("test_toc")
                .weight(100)
                .sizeDepth(100)
                .sizeHeight(100)
                .sizeWidth(100)
                .priceSales(10000)
                .priceStandard(10000)
                .starRating(4.5)
                .reviewCount(100)
                .category("test_category")
                .categoryId(9)
                .translators(List.of(
                        BookCreateServiceRequest.Translator.builder()
                                .translatorId(10)
                                .name("test_translator1")
                                .build(),
                        BookCreateServiceRequest.Translator.builder()
                                .translatorId(11)
                                .name("test_translator2")
                                .build()
                ))
                .authors(List.of(
                        BookCreateServiceRequest.Author.builder()
                                .authorId(12)
                                .name("test_author1")
                                .build(),
                        BookCreateServiceRequest.Author.builder()
                                .authorId(13)
                                .name("test_author2")
                                .build()
                ))
                .build();
    }

    public static BookDetailResponse createBookDetailServiceResponse() {
        return BookDetailResponse.builder()
                .title("어린 왕자")
                .subTitle("어린 왕자 부제")
                .author("앙투안 드 생텍쥐페리 지은이")
                .thumbnail("https://image.aladin.co.kr/product/6853/49/coversum/8932917248_2.jpg")
                .link("http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=68534943&amp;partner=openAPI&amp;start=api")
                .isbn10("8932917248")
                .isbn13("9788932917245")
                .authors(List.of(BookDetailResponse.Author.builder()
                        .name("앙투안 드 생텍쥐페리")
                        .authorId(20310)
                        .build()))
                .translators(List.of(BookDetailResponse.Translator.builder()
                        .name("황현산")
                        .translatorId(139607)
                        .build()))
                .starRating(4.5)
                .reviewCount(100)
                .aladinReviewCount(100)
                .aladinStarRating(3.5)
                .publicationDate("2015-10-20")
                .category("국내도서>소설/시/희곡>프랑스소설")
                .categoryId(50921)
                .pages(136)
                .publisher("열린책들")
                .description(
                        "전 세계인들의 사랑을 받은 가장 아름다운 이야기, 생텍쥐페리의 &lt;어린 왕자&gt;가 문학 평론가 황현산의 번역으로 열린책들에서 출간되었다.")
                .toc("<p>목차 없는 상품입니다.</p>")
                .weight(490)
                .sizeDepth(13)
                .sizeHeight(220)
                .sizeWidth(157)
                .priceSales(10620)
                .priceStandard(11800)
                .interestCount(10)
                .holderCount(10)
                .readCount(10)
                .build();
    }

    public static BookDetailServiceRequest createBookDetailServiceRequest() {
        return BookDetailServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .isbn10("1111111111")
                .isbn13("1111111111111")
                .build();
    }

    public static BookInterestServiceRequest createBookInterestServiceRequest() {
        return BookInterestServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .isbn13("1111111111111")
                .build();
    }

    public static BookInterestHandleResponse createBookInterestHandleResponse() {
        return BookInterestHandleResponse.builder()
                .userId("LOGIN_USER_ID")
                .isbn13("1111111111111")
                .interested(true)
                .build();
    }

    public static BookMyInterestFindServiceRequest createBookMyInterestFindServiceRequest() {
        return BookMyInterestFindServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .bookOrderType(BookOrderType.TITLE)
                .build();
    }

    public static BookInterestElementResponse createBookInterestElementResponse() {
        return BookInterestElementResponse.builder()
                .id(1L)
                .title("test_title")
                .isbn13("9731111111111")
                .thumbnailUrl("test_thumbnailUrl")
                .author("test_author")
                .build();
    }

    public static BookInterestStatusServiceRequest createBookInterestStatusServiceRequest() {
        return BookInterestStatusServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .isbn13("1111111111111")
                .build();
    }

    public static BookInterestStatusResponse createBookInterestStatusResponse() {
        return BookInterestStatusResponse.builder()
                .interested(true)
                .build();
    }
}
