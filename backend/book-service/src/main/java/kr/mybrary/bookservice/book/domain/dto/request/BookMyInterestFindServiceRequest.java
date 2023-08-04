package kr.mybrary.bookservice.book.domain.dto.request;

import kr.mybrary.bookservice.book.persistence.OrderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookMyInterestFindServiceRequest {

    private String loginId;
    private OrderType orderType;
}
