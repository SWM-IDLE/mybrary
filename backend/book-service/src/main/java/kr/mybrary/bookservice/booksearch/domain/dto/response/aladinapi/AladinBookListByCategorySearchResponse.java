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
public class AladinBookListByCategorySearchResponse {

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
        private Integer priceSales;
        private Integer priceStandard;
        private String mallType;
        private String stockStatus;
        private Integer mileage;
        private String cover;
        private String publisher;
        private Integer salesPoint;
        private Boolean adult;
        private Boolean fixedPrice;
        private Integer customerReviewRank;
        private String bestDuration;
        private Integer bestRank;
    }
}
