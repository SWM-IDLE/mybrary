package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookSearchDetailResponse {

    private String title;
    private String subTitle;
    private String thumbnail;
    private String link;
    private String author;
    private List<Author> authors;
    private List<Translator> translators;
    private Double starRating;
    private Integer reviewCount;
    private String publicationDate;
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
    private Integer holderCount;
    private Integer readCount;
    private Integer interestCount;

    @Builder
    @Getter
    public static class Author {
        private String name;
        private Integer authorId;

    }

    @Builder
    @Getter
    public static class Translator {
        private String name;
        private Integer translatorId;
    }

}
