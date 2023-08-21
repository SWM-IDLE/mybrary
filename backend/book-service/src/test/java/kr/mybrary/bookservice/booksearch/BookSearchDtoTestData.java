package kr.mybrary.bookservice.booksearch;

import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookListByCategorySearchServiceRequest;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategoryResponseElement;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchRankingResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchResultResponseElement;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.aladinapi.AladinBookSearchResponse;
import kr.mybrary.bookservice.booksearch.domain.dto.response.kakaoapi.KakaoBookSearchResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookListByCategorySearchResultResponse;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;

public class BookSearchDtoTestData {

    public static BookSearchResultResponseElement createBookSearchDto() {
        return BookSearchResultResponseElement.builder()
                .title("자바의 정석")
                .description("자바의 정석 3판")
                .isbn13("9788980782970")
                .author("남궁성")
                .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/150/077/15007773.jpg?type=m1&udate=20180726")
                .starRating(0.0)
                .publicationDate("2008-08-01")
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
                        .isbn("8980782977 9788980782970")
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

    public static BookSearchDetailResponse createBookSearchDetailResponse() {
        return BookSearchDetailResponse.builder()
                .title("어린 왕자")
                .subTitle("어린 왕자 부제")
                .author("앙투안 드 생텍쥐페리 (지은이)")
                .thumbnail("https://image.aladin.co.kr/product/6853/49/coversum/8932917248_2.jpg")
                .link("http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=68534943&amp;partner=openAPI&amp;start=api")
                .isbn10("8932917248")
                .isbn13("9788932917245")
                .authors(List.of(BookSearchDetailResponse.Author.builder()
                        .name("앙투안 드 생텍쥐페리")
                        .authorId(20310)
                        .build()))
                .translators(List.of(BookSearchDetailResponse.Translator.builder()
                        .name("황현산")
                        .translatorId(139607)
                        .build()))
                .starRating(4.5)
                .reviewCount(100)
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

    public static AladinBookSearchDetailResponse.Item createAladinBookSearchDetailResponseItem() {
        return AladinBookSearchDetailResponse.Item.builder()
                .title("Sample Book Title - Sub Title")
                .link("https://sample-link.com")
                .author("John Doe")
                .pubDate("2023-07-27")
                .description("Sample book description.")
                .isbn("1234567890")
                .isbn13("9781234567890")
                .itemId(1)
                .priceSales(10000)
                .priceStandard(12000)
                .mallType("Sample Mall")
                .stockStatus("In Stock")
                .mileage(100)
                .cover("https://sample-cover.com")
                .categoryId(1)
                .categoryName("Fiction")
                .publisher("Sample Publisher")
                .salesPoint(500)
                .adult(false)
                .fixedPrice(true)
                .customerReviewRank(4)
                .fullDescription("Full book description.")
                .fullDescription2("Another full book description.")
                .subInfo(buildSubInfo())
                .build();
    }

    public static AladinBookSearchDetailResponse.SubInfo buildSubInfo() {
        return AladinBookSearchDetailResponse.SubInfo.builder()
                .subTitle("Sub Title")
                .originalTitle("Original Title")
                .itemPage(100)
                .toc("Table of Contents")
                .authors(List.of(buildAuthor(), buildTranslator()))
                .ratingInfo(buildRatingInfo())
                .packing(buildPacking())
                .build();
    }

    public static AladinBookSearchDetailResponse.Author buildAuthor() {
        return AladinBookSearchDetailResponse.Author.builder()
                .authorId(1)
                .authorName("John Doe")
                .authorType("author")
                .authorTypeDesc("Sample Type Description")
                .authorInfo("Author Information")
                .authorInfoLink("https://sample-author-info.com")
                .authorPhoto("https://sample-author-photo.com")
                .build();
    }
    public static AladinBookSearchDetailResponse.Author buildTranslator() {
        return AladinBookSearchDetailResponse.Author.builder()
                .authorId(1)
                .authorName("John Doe")
                .authorType("translator")
                .authorTypeDesc("Sample Type Description")
                .authorInfo("translator Information")
                .authorInfoLink("https://sample-author-info.com")
                .authorPhoto("https://sample-author-photo.com")
                .build();
    }

    public static AladinBookSearchDetailResponse.RatingInfo buildRatingInfo() {
        return AladinBookSearchDetailResponse.RatingInfo.builder()
                .ratingScore(4.5)
                .ratingCount(100)
                .commentReviewCount(50)
                .myReviewCount(10)
                .build();
    }

    public static AladinBookSearchDetailResponse.Packing buildPacking() {
        return AladinBookSearchDetailResponse.Packing.builder()
                .styleDesc("Sample Packing Style")
                .weight(300)
                .sizeDepth(20)
                .sizeHeight(30)
                .sizeWidth(10)
                .build();
    }

    public static BookListByCategorySearchServiceRequest createBookListSearchServiceRequest() {
        return BookListByCategorySearchServiceRequest.builder()
                .page(1)
                .type("bestseller")
                .categoryId(0)
                .build();
    }

    public static BookListByCategorySearchResultResponse createBookListSearchResultResponse() {
        return BookListByCategorySearchResultResponse.builder()
                .books(List.of(createBookListByCategoryServiceResponse()))
                .build();
    }

    public static BookListByCategoryResponseElement createBookListByCategoryServiceResponse() {
        return BookListByCategoryResponseElement.builder()
                .thumbnailUrl("test_thumbnail_url")
                .isbn13("test_isbn13")
                .build();
    }

    public static BookSearchRankingResponse createBookSearchRankingResponse() {
        return BookSearchRankingResponse.builder()
                .bookSearchKeywords(List.of(
                        BookSearchRankingResponse.BookSearchRanking.builder()
                                .keyword("사랑")
                                .score(10.0)
                                .build(),
                        BookSearchRankingResponse.BookSearchRanking.builder()
                                .keyword("미움")
                                .score(5.0)
                                .build(),
                        BookSearchRankingResponse.BookSearchRanking.builder()
                                .keyword("감동")
                                .score(3.0)
                                .build()))
                .build();
    }
}
