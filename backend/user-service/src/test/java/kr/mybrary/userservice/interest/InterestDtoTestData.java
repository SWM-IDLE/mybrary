package kr.mybrary.userservice.interest;

import java.util.List;
import java.util.stream.IntStream;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse.BookRecommendationsResponseElement;

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

}
