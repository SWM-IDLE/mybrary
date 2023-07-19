package kr.mybrary.bookservice.mybook.domain.dto.request;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
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
}
