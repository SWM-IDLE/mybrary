package kr.mybrary.bookservice.tag.domain.dto.request;

import kr.mybrary.bookservice.mybook.persistence.MyBook;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeaningTagAssignServiceRequest {

    private MyBook myBook;
    private String loginId;
    private String quote;
    private String colorCode;
}
