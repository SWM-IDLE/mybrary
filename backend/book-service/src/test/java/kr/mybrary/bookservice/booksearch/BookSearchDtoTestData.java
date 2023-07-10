package kr.mybrary.bookservice.booksearch;

import java.time.OffsetDateTime;
import java.util.List;
import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchResultDto;

public class BookSearchDtoTestData {

    public static BookSearchResultDto createBookSearchDto() {
        return BookSearchResultDto.builder()
                .title("자바의 정석")
                .detailsUrl("자바의 정석 detail Url")
                .description("자바의 정석 3판")
                .isbn10("8980782970")
                .isbn13("9788980782970")
                .authors(List.of("남궁성"))
                .translators(List.of("박은종"))
                .salePrice(25000)
                .price(25000)
                .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/150/077/15007773.jpg?type=m1&udate=20180726")
                .status("정상판매")
                .starRating(0.0)
                .publisher("도우출판")
                .publicationDate(OffsetDateTime.parse("2008-08-01T00:00:00+09:00"))
                .build();
    }

}
