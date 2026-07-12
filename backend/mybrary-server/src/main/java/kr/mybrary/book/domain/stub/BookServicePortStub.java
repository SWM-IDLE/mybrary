package kr.mybrary.book.domain.stub;

import java.util.Collections;
import kr.mybrary.book.domain.BookServicePort;
import kr.mybrary.book.domain.dto.response.BookRecommendationsServiceResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * BookServicePort 스텁 구현체.
 * P1-03(book 도메인 이전) 이전까지 non-prod 환경에서 사용된다.
 * prod 환경에서는 P1-04에서 실제 구현체가 등록될 예정이다.
 */
@Component
@Profile("!prod")
public class BookServicePortStub implements BookServicePort {

    @Override
    public BookRecommendationsServiceResponse getBookListByCategoryId(String type, int categoryId, int page) {
        // P1-03/P1-04에서 실제 book 도메인 구현으로 대체 예정
        return BookRecommendationsServiceResponse.builder()
                .data(BookRecommendationsServiceResponse.Data.builder()
                        .books(Collections.emptyList())
                        .build())
                .build();
    }
}
