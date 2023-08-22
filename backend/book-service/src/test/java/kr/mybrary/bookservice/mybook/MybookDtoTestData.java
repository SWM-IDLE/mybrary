package kr.mybrary.bookservice.mybook;

import java.time.LocalDateTime;
import java.util.List;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookReadCompletedStatusServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookRegisteredStatusServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest.MybookUpdateServiceRequestBuilder;
import kr.mybrary.bookservice.mybook.persistence.MyBookOrderType;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.mybook.persistence.model.MyBookListDisplayElementModel;
import kr.mybrary.bookservice.mybook.persistence.model.MyBookListDisplayElementModel.MyBookListDisplayElementModelBuilder;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookCreateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.request.MyBookUpdateRequest;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse.BookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementFromMeaningTagResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse.BookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookReadCompletedStatusResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookRegisteredStatusResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookRegistrationCountResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookUpdateResponse;

public class MybookDtoTestData {

    public static MyBookCreateRequest createMyBookCreateRequest() {
        return MyBookCreateRequest.builder()
                .isbn13("isbn13")
                .build();
    }

    public static MyBookElementResponse createMyBookElementResponse() {
        return MyBookElementResponse.builder()
                .id(1L)
                .shareable(true)
                .startDateOfPossession("2020.01.01")
                .exchangeable(true)
                .showable(true).readStatus(ReadStatus.TO_READ)
                .book(BookElementResponse.builder()
                        .id(1L)
                        .title("토비의 스프링 3.1")
                        .description("스프링의 기본기를 다지기 위한 책")
                        .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/137/995/13799585.jpg?type=m1&udate=20191226")
                        .starRating(5.0)
                        .publicationDate("2012.01.01")
                        .authors("저자_1, 저자_2")
                        .build())
                .build();
    }

    public static MyBookElementFromMeaningTagResponse createMyBookElementFromMeaningTagResponse() {
        return MyBookElementFromMeaningTagResponse.builder()
                .id(1L)
                .shareable(true)
                .startDateOfPossession("2020.01.01")
                .exchangeable(true)
                .showable(true).readStatus(ReadStatus.TO_READ)
                .book(MyBookElementFromMeaningTagResponse.BookElementResponse.builder()
                        .id(1L)
                        .title("토비의 스프링 3.1")
                        .description("스프링의 기본기를 다지기 위한 책")
                        .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/137/995/13799585.jpg?type=m1&udate=20191226")
                        .starRating(5.0)
                        .publicationDate("2012.01.01")
                        .build())
                .build();
    }

    public static MyBookDetailResponse createMyBookDetailResponse() {
        return MyBookDetailResponse.builder()
                .id(1L)
                .shareable(true)
                .exchangeable(true)
                .showable(true)
                .startDateOfPossession("2020.01.01")
                .readStatus(ReadStatus.TO_READ)
                .book(BookDetailResponse.builder()
                        .id(1L)
                        .isbn13("9788960773417")
                        .title("토비의 스프링 3.1")
                        .description("스프링의 기본기를 다지기 위한 책")
                        .authors(List.of("토비"))
                        .translators(List.of("토비"))
                        .publisher("토비의 출판사")
                        .thumbnailUrl("https://bookthumb-phinf.pstatic.net/cover/137/995/13799585.jpg?type=m1&udate=20191226")
                        .starRating(5.0)
                        .build()
                )
                .meaningTag(MyBookDetailResponse.MeaningTag.builder()
                        .quote("the book that help me to understand spring")
                        .colorCode("#FFFFFF")
                        .build()
                )
                .build();
    }

    public static MyBookCreateServiceRequest createMyBookCreateServiceRequest() {
        return MyBookCreateServiceRequest.builder()
                .userId("test1")
                .isbn13("isbn13")
                .build();
    }

    public static MyBookFindAllServiceRequest createMyBookFindAllServiceRequest(String userId, String loginId) {
        return MyBookFindAllServiceRequest.builder()
                .userId(userId)
                .loginId(loginId)
                .myBookOrderType(MyBookOrderType.TITLE)
                .readStatus(ReadStatus.READING)
                .build();
    }

    public static MyBookUpdateRequest createMyBookUpdateRequest() {
        return MyBookUpdateRequest.builder()
                .showable(true)
                .exchangeable(false)
                .shareable(true)
                .readStatus(ReadStatus.READING)
                .startDateOfPossession(LocalDateTime.now())
                .meaningTag(MyBookUpdateRequest.MeaningTag.builder()
                        .quote("the book that help me dream")
                        .colorCode("#FFFFFF")
                        .build())
                .build();
    }

    public static MyBookUpdateResponse createMyBookUpdateResponse() {
        return MyBookUpdateResponse.builder()
                .showable(true)
                .exchangeable(false)
                .shareable(true)
                .readStatus(ReadStatus.READING)
                .startDateOfPossession(LocalDateTime.now())
                .meaningTag(MyBookUpdateResponse.MeaningTag.builder()
                        .quote("the book that help me dream")
                        .colorCode("#FFFFFF")
                        .build())
                .build();
    }

    public static MybookUpdateServiceRequestBuilder createMyBookUpdateServiceRequest(String loginId, Long myBookId) {
        return MybookUpdateServiceRequest.builder()
                .loginId(loginId)
                .myBookId(myBookId)
                .showable(true)
                .exchangeable(false)
                .shareable(true)
                .readStatus(ReadStatus.READING)
                .startDateOfPossession(LocalDateTime.now())
                .meaningTag(MybookUpdateServiceRequest.MeaningTag.builder()
                        .quote("the book that help me dream")
                        .colorCode("#FFFFFF")
                        .build());
    }

    public static MyBookListDisplayElementModelBuilder createMyBookListDisplayElementModelBuilder() {
        return MyBookListDisplayElementModel.builder()
                .myBookId(1L)
                .shareable(true)
                .startDateOfPossession(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .exchangeable(true)
                .showable(true)
                .readStatus(ReadStatus.TO_READ)
                .bookId(1L)
                .title("test_title")
                .description("test_description")
                .thumbnailUrl("test_thumbnail_url")
                .starRating(5.0)
                .publicationDate(LocalDateTime.of(2023, 1, 2, 0, 0, 0))
                .bookAuthors(List.of(
                        BookAuthor.builder()
                                .author(Author.builder().name("저자_1").build())
                                .build(),
                        BookAuthor.builder()
                                .author(Author.builder().name("저자_2").build())
                                .build()));
    }

    public static MyBookRegistrationCountResponse createMyBookRegistrationCountResponse() {
        return MyBookRegistrationCountResponse.builder()
                .count(10L)
                .build();
    }

    public static MyBookRegisteredStatusServiceRequest createMyBookRegisteredStatusServiceRequest() {
        return MyBookRegisteredStatusServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .isbn13("1111111111111")
                .build();
    }

    public static MyBookRegisteredStatusResponse createMyBookRegisteredStatusResponse() {
        return MyBookRegisteredStatusResponse.builder()
                .registered(true)
                .build();
    }

    public static MyBookReadCompletedStatusServiceRequest createMyBookReadCompletedStatusServiceRequest() {
        return MyBookReadCompletedStatusServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .isbn13("1111111111111")
                .build();
    }

    public static MyBookReadCompletedStatusResponse createMyBookReadCompletedStatusResponse() {
        return MyBookReadCompletedStatusResponse.builder()
                .completed(true)
                .build();
    }
}
