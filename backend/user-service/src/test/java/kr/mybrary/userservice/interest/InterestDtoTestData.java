package kr.mybrary.userservice.interest;

import java.util.List;
import java.util.stream.IntStream;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse.BookRecommendationsResponseElement;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestAndBookRecommendationsResponse;

public class InterestDtoTestData {

    public static BookRecommendationsServiceResponse createBookRecommendationsServiceResponse() {

        List<BookRecommendationsResponseElement> list = IntStream.range(0, 10)
                .mapToObj(i -> BookRecommendationsResponseElement.builder()
                        .isbn13("isbn13_" + i)
                        .thumbnailUrl("thumbnailUrl_" + i)
                        .build())
                .toList();

        return BookRecommendationsServiceResponse.builder()
                .data(BookRecommendationsServiceResponse.Data.builder()
                        .books(list)
                        .build())
                .build();
    }

    public static UserInterestAndBookRecommendationsResponse createUserInterestAndBookRecommendationsResponse() {
        return UserInterestAndBookRecommendationsResponse.builder()
                .userInterests(List.of(
                        UserInterestAndBookRecommendationsResponse.UserInterestElement.builder()
                                .name("name_1")
                                .code(1)
                                .build(),
                        UserInterestAndBookRecommendationsResponse.UserInterestElement.builder()
                                .name("name_2")
                                .code(2)
                                .build()
                ))
                .bookRecommendations(List.of(
                        UserInterestAndBookRecommendationsResponse.BookRecommendationElement.builder()
                                .thumbnailUrl("thumbnailUrl_1")
                                .isbn13("isbn13_1")
                                .build(),
                        UserInterestAndBookRecommendationsResponse.BookRecommendationElement.builder()
                                .thumbnailUrl("thumbnailUrl_2")
                                .isbn13("isbn13_2")
                                .build()
                ))
                .build();
    }
}
