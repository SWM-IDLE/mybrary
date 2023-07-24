package kr.mybrary.bookservice.tag;

import kr.mybrary.bookservice.mybook.MyBookFixture;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagAssignServiceRequest;
import kr.mybrary.bookservice.tag.domain.dto.request.MeaningTagFindPageServiceRequest;
import kr.mybrary.bookservice.tag.presentation.dto.response.MeaningTagElementResponse;

public class MeaningTagDtoTestData {

    public static MeaningTagAssignServiceRequest createMeaningTagAssignServiceRequest() {
        return MeaningTagAssignServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook())
                .quote("TEST_QUOTE")
                .colorCode("#TEST_COLOR_CODE")
                .build();
    }
    public static MeaningTagAssignServiceRequest createMeaningTagAssignServiceRequestWithEmptyQuote() {
        return MeaningTagAssignServiceRequest.builder()
                .loginId("LOGIN_USER_ID")
                .myBook(MyBookFixture.COMMON_LOGIN_USER_MYBOOK.getMyBook())
                .quote("")
                .colorCode("#TEST_COLOR_CODE")
                .build();
    }

    public static MeaningTagFindPageServiceRequest createMeaningTagFindPageServiceRequest(int size) {
        return MeaningTagFindPageServiceRequest.builder()
                .size(size)
                .build();
    }

    public static MeaningTagElementResponse createMeaningTagElementResponse(Long id, int registeredCount) {
        return MeaningTagElementResponse.builder()
                .id(id)
                .quote("TEST_QUOTE")
                .registeredCount(registeredCount)
                .build();
    }
}
