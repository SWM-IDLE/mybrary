package kr.mybrary.bookservice.book.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookIsInterestedServiceRequest {

    private String isbn13;
    private String userId;

    public static BookIsInterestedServiceRequest of(String isbn13, String userId) {
        return BookIsInterestedServiceRequest.builder()
                .isbn13(isbn13)
                .userId(userId)
                .build();
    }
}
