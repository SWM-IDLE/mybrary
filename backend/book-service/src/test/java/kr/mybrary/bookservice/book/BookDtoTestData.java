package kr.mybrary.bookservice.book;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.presentation.dto.request.BookCreateRequest;

public class BookDtoTestData {

    public static BookCreateRequest createBookCreateRequest() {
        return BookCreateRequest.builder()
                .title("test_title")
                .subTitle("test_subTitle")
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
}
