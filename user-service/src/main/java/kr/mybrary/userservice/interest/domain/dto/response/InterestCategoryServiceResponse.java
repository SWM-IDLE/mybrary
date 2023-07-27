package kr.mybrary.userservice.interest.domain.dto.response;

import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.InterestCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class InterestCategoryServiceResponse {

    Map<InterestCategory, List<Interest>> interestsGroupByCategory;

}
