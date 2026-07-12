package kr.mybrary.book.domain;

import kr.mybrary.book.domain.dto.response.BookRecommendationsServiceResponse;

/**
 * book-service의 도서 추천 API를 추상화한 포트 인터페이스.
 * P1-03(book 도메인 이전) 이후 실제 구현체로 교체된다.
 * 현재는 BookServicePortStub(스텁)이 non-prod 환경에서 빈으로 등록된다.
 */
public interface BookServicePort {

    BookRecommendationsServiceResponse getBookListByCategoryId(String type, int categoryId, int page);

}
