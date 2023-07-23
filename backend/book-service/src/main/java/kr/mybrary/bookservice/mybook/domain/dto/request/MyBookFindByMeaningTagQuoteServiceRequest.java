package kr.mybrary.bookservice.mybook.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookFindByMeaningTagQuoteServiceRequest {

    private String quote;

    public static MyBookFindByMeaningTagQuoteServiceRequest of(String quote) {
        return MyBookFindByMeaningTagQuoteServiceRequest.builder()
                .quote(quote)
                .build();
    }
}
