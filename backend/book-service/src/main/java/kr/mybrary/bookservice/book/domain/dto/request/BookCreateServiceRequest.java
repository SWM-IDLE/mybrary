package kr.mybrary.bookservice.book.domain.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateServiceRequest {

    private String title;
    private String subTitle;
    private String thumbnailUrl;
    private String link;
    private String author;
    private List<Author> authors;
    private List<Translator> translators;
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

}
