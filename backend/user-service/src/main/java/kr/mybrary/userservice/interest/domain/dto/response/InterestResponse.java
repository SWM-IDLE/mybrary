package kr.mybrary.userservice.interest.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterestResponse {

    private Long id;
    private String name;

}
