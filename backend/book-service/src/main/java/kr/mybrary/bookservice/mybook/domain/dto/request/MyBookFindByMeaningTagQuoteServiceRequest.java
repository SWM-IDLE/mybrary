package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookFindByMeaningTagQuoteServiceRequest {

    private String loginId;
    private String quote;

    public static MyBookFindByMeaningTagQuoteServiceRequest of(String loginId, String quote) {
        return MyBookFindByMeaningTagQuoteServiceRequest.builder()
                .loginId(loginId)
                .quote(quote)
                .build();
    }
}
