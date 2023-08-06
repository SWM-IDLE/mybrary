package kr.mybrary.bookservice.book.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateRequest {

    private String title;
    private String subTitle;
    private String author;
    private String thumbnailUrl;
    private String link;
    private List<BookCreateServiceRequest.Author> authors;
    private List<BookCreateServiceRequest.Translator> translators;
    private Double starRating;
    private Integer reviewCount;
    private LocalDateTime publicationDate;
    private String category;
    private Integer categoryId;
    private Integer pages;
    private String publisher;
    private String description;
    private String toc;
    private String isbn10;
    private String isbn13;
    private Integer weight;
    private Integer sizeDepth;
    private Integer sizeHeight;
    private Integer sizeWidth;
    private Integer priceSales;
    private Integer priceStandard;

    @Getter
    @Builder
    public static class Author {
        private String name;
        private Integer authorId;
    }

    @Getter
    @Builder
    public static class Translator {
        private String name;
        private Integer translatorId;
    }

    public BookCreateServiceRequest toServiceRequest() {
        return BookCreateServiceRequest.builder()
                .title(title)
                .subTitle(subTitle)
                .thumbnailUrl(thumbnailUrl)
                .link(link)
                .authors(authors)
                .translators(translators)
                .starRating(starRating)
                .reviewCount(reviewCount)
                .publicationDate(publicationDate)
                .category(category)
                .categoryId(categoryId)
                .pages(pages)
                .publisher(publisher)
                .description(description)
                .toc(toc)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .weight(weight)
                .sizeDepth(sizeDepth)
                .sizeHeight(sizeHeight)
                .sizeWidth(sizeWidth)
                .priceSales(priceSales)
                .priceStandard(priceStandard)
                .build();
    }
}
