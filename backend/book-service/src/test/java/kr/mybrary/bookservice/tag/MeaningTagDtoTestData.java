package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;

public class MeaningTagDtoTestData {

    public static MeaningTagAssignServiceRequest createMeaningTagAssignServiceRequest() {
        return MeaningTagAssignServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook())
                .quote("TEST_QUOTE")
                .colorCode("#TEST_COLOR_CODE")
                .build();
    }

}
