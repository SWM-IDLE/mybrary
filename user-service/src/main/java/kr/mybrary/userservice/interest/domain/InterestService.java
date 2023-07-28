package kr.mybrary.userservice.interest.domain;

import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;

public interface InterestService {

    InterestCategoryServiceResponse getInterestCategories();

}
