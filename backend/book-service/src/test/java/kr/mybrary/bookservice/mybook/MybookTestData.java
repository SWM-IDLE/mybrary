package kr.mybrary.bookservice.mybook;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse.BookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse.BookElementResponse;

public class MybookTestData {

    public static MyBookCreateRequest createMyBookCreateRequest() {
        return MyBookCreateRequest.builder()
                .title("title")
                .description("description")
                .detailsUrl("detailsUrl")
                .isbn10("isbn10")
                .isbn13("isbn13")
                .publisher("publisher")
                .price(10000)
                .publicationDate(LocalDateTime.now())
                .translators(List.of("translator1", "translator2"))
                .authors(List.of("author1", "author2"))
                .thumbnailUrl("thumbnailUrl")
                .build();
    }

    public static MyBookElementResponse createMyBookElementResponse() {
        return MyBookElementResponse.builder()
                .id(1L)
                .isShareable(true)
                .startDateOfPossession(LocalDateTime.now())
                .isExchangeable(true)
                .isPublic(true).readStatus(ReadStatus.TO_READ)
                .book(BookElementResponse.builder()
                        .id(1L)
                        .title("토비의 스프링 3.1")
                        .description("스프링의 기본기를 다지기 위한 책")
                        .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/137/995/13799585.jpg?type=m1&udate=20191226")
                        .stars(5)
                        .build())
                .build();
    }

    public static MyBookDetailResponse createMyBookDetailResponse() {
        return MyBookDetailResponse.builder()
                .id(1L)
                .isShareable(true)
                .isExchangeable(true)
                .isPublic(true)
                .startDateOfPossession(LocalDateTime.now())
                .readStatus(ReadStatus.TO_READ)
                .book(BookDetailResponse.builder()
                        .id(1L)
                        .title("토비의 스프링 3.1")
                        .description("스프링의 기본기를 다지기 위한 책")
                        .authors(List.of("토비"))
                        .translators(List.of("토비"))
                        .publisher("토비의 출판사")
                        .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/137/995/13799585.jpg?type=m1&udate=20191226")
                        .stars(5)
                        .build()
                )
                .build();
    }
}
