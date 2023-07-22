package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagFindPageServiceRequest;

public class MeaningTagDtoTestData {

    public static MeaningTagAssignServiceRequest createMeaningTagAssignServiceRequest() {
        return MeaningTagAssignServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook())
                .quote("TEST_QUOTE")
                .colorCode("#TEST_COLOR_CODE")
                .build();
    }

    public static MeaningTagFindPageServiceRequest createMeaningTagFindPageServiceRequest(int size) {
        return MeaningTagFindPageServiceRequest.builder()
                .size(size)
                .build();
    }
}
