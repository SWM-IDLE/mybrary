package kr.mybrary.bookservice.tag.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeaningTagElementResponse {

    private Long id;
    private String quote;
    private int registeredCount;
}
