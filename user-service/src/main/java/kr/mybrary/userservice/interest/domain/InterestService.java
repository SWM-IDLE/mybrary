package kr.mybrary.userservice.interest.domain;

import kr.mybrary.userservice.interest.domain.dto.response.InterestsGroupByCategoryServiceResponse;

public interface InterestService {

    InterestsGroupByCategoryServiceResponse getInterestsGroupByCategory();

}
