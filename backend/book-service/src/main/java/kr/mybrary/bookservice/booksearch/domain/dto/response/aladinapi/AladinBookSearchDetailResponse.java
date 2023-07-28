package kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AladinBookSearchDetailResponse {

    private String version;
    private String logo;
    private String title;
    private String link;
    private String pubDate;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private String query;
    private int searchCategoryId;
    private String searchCategoryName;
    private List<Item> item;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String title;
        private String link;
        private String author;
        private String pubDate;
        private String description;
        private String isbn;
        private String isbn13;
        private int itemId;
        private int priceSales;
        private int priceStandard;
        private String mallType;
        private String stockStatus;
        private int mileage;
        private String cover;
        private int categoryId;
        private String categoryName;
        private String publisher;
        private int salesPoint;
        private boolean adult;
        private boolean fixedPrice;
        private int customerReviewRank;
        private String fullDescription;
        private String fullDescription2;
        private SubInfo subInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubInfo {
        private String subTitle;
        private String originalTitle;
        private int itemPage;
        private String toc;
        private List<Author> authors;
        private RatingInfo ratingInfo;
        private Packing packing;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author {
        private int authorId;
        private String authorName;
        private String authorType;
        private String authorTypeDesc;
        private String authorInfo;
        private String authorInfoLink;
        private String authorPhoto;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingInfo {
        private double ratingScore;
        private int ratingCount;
        private int commentReviewCount;
        private int myReviewCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Packing {
        private String styleDesc;
        private int weight;
        private int sizeDepth;
        private int sizeHeight;
        private int sizeWidth;
    }
}