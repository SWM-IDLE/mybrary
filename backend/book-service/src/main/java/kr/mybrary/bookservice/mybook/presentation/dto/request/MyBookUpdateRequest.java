package kr.mybrary.bookservice.mybook.presentation.dto.request;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookUpdateRequest {

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

    public MybookUpdateServiceRequest toServiceRequest(String userId, Long myBookId) {
        return MybookUpdateServiceRequest.builder()
                .loginId(userId)
                .myBookId(myBookId)
                .showable(showable)
                .exchangeable(exchangeable)
                .shareable(shareable)
                .readStatus(readStatus)
                .startDateOfPossession(startDateOfPossession)
                .meaningTag(MybookUpdateServiceRequest.MeaningTag.builder()
                        .quote(meaningTag.quote)
                        .colorCode(meaningTag.colorCode)
                        .build())
                .build();
    }
}
