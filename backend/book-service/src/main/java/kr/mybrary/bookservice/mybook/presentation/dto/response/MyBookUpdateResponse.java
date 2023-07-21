package kr.mybrary.bookservice.mybook.presentation.dto.response;

import java.time.LocalDateTime;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyBookUpdateResponse {

    private ReadStatus readStatus;

    private LocalDateTime startDateOfPossession;

    private boolean showable;
    private boolean exchangeable;
    private boolean shareable;

    private MeaningTag meaningTag;

    @Getter
    @Builder
    public static class MeaningTag {
        private String quote;
        private String colorCode;
    }

    public static MyBookUpdateResponse of(MyBook myBook, MybookUpdateServiceRequest.MeaningTag meaningTag) {
        return MyBookUpdateResponse.builder()
                .readStatus(myBook.getReadStatus())
                .startDateOfPossession(myBook.getStartDateOfPossession())
                .showable(myBook.isShowable())
                .exchangeable(myBook.isExchangeable())
                .shareable(myBook.isShareable())
                .meaningTag(MeaningTag.builder()
                        .quote(meaningTag.getQuote())
                        .colorCode(meaningTag.getColorCode())
                        .build())
                .build();
    }
}
