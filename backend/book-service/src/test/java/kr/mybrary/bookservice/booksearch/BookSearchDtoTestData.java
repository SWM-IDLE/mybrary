package kr.mybrary.bookservice.booksearch;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.response.BookSearchResultServiceResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;

public class BookSearchDtoTestData {

    public static BookSearchResultServiceResponse createBookSearchDto() {
        return BookSearchResultServiceResponse.builder()
                .title("자바의 정석")
                .description("자바의 정석 3판")
                .isbn13("9788980782970")
                .author("남궁성")
                .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/150/077/15007773.jpg?type=m1&udate=20180726")
                .starRating(0.0)
                .publicationDate("2008-08-01T00:00:00+09:00")
                .build();
    }

    public static KakaoBookSearchResponse createKakaoBookSearchResponse() {
        return KakaoBookSearchResponse.builder()
                .meta(KakaoBookSearchResponse.Meta.builder()
                        .total_count(100)
                        .pageable_count(10)
                        .is_end(false)
                        .build())
                .documents(List.of(KakaoBookSearchResponse.Document.builder()
                        .title("kakao api title")
                        .contents("kakao api contents")
                        .url("kakao api url")
                        .isbn("9788980782970")
                        .datetime("2021-07-08T00:00:00.000+09:00")
                        .authors(List.of("kakao api author1", "kakao api author2"))
                        .publisher("kakao api publisher")
                        .translators(List.of("kakao api translator1", "kakao api translator2"))
                        .price(10000)
                        .sale_price(10000)
                        .thumbnail("kakao api thumbnail")
                        .status("kakao api status")
                        .build()))
                .build();
    }

    public static AladinBookSearchResponse createAladinBookSearchResponse() {
        return AladinBookSearchResponse.builder()
                .version("20131101")
                .title("aladin api title")
                .link("aladin api link")
                .pubDate("aladin api pubDate")
                .totalResults(100)
                .startIndex(1)
                .itemsPerPage(10)
                .query("aladin api query")
                .searchCategoryId(1)
                .searchCategoryName("aladin api searchCategoryName")
                .item(List.of(AladinBookSearchResponse.Item.builder()
                        .title("aladin api item title")
                        .link("aladin api item link")
                        .author("aladin api item author")
                        .pubDate("aladin api item pubDate")
                        .description("aladin api item description")
                        .isbn("aladin api item isbn")
                        .isbn13("aladin api item isbn13")
                        .priceSales(10000)
                        .priceStandard(10000)
                        .mallType("aladin api item mallType")
                        .stockStatus("aladin api item stockStatus")
                        .mileage(100)
                        .cover("aladin api item cover")
                        .publisher("aladin api item publisher")
                        .salesPoint(100)
                        .adult(false)
                        .fixedPrice(false)
                        .customerReviewRank(100)
                        .bestDuration("aladin api item bestDuration")
                        .bestRank(100)
                        .seriesInfo(AladinBookSearchResponse.SeriesInfo.builder()
                                .seriesId("aladin api item seriesId")
                                .seriesLink("aladin api item seriesLink")
                                .seriesName("aladin api item seriesName")
                                .build())
                        .build()))
                .build();
    }

}
