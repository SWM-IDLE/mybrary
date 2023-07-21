package kr.mybrary.bookservice.mybook.domain.dto.request;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MybookUpdateServiceRequest {

    private String loginId;
    private Long myBookId;

    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;
    private ReadStatus readStatus;
    private LocalDateTime startDateOfPossession;

    private MeaningTag meaningTag;

    @Getter
    @Builder
    public static class MeaningTag {
        private String quote;
        private String colorCode;
    }

    public MeaningTagAssignServiceRequest toMeaningTagAssignServiceRequest(MyBook myBook) {
        return MeaningTagAssignServiceRequest.builder()
                .myBook(myBook)
                .loginId(loginId)
                .quote(meaningTag.getQuote())
                .colorCode(meaningTag.getColorCode())
                .build();
    }
}
