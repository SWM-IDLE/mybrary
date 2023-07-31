package kr.mybrary.bookservice.mybook.presentation.dto.request;

import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBookCreateRequest {

    private String isbn13;

    public MyBookCreateServiceRequest toServiceRequest(String userId) {
        return MyBookCreateServiceRequest.builder()
                .userId(userId)
                .isbn13(isbn13)
                .build();
    }
}
