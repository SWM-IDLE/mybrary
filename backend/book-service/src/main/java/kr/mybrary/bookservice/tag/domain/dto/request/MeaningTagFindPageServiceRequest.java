package kr.mybrary.bookservice.tag.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeaningTagFindPageServiceRequest {

    private int size;

    public static MeaningTagFindPageServiceRequest of(int size) {
        return MeaningTagFindPageServiceRequest.builder()
                .size(size)
                .build();
    }
}
