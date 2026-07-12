package kr.mybrary.interest.domain;

import kr.mybrary.interest.domain.dto.request.UserInterestAndBookRecommendationsServiceRequest;
import kr.mybrary.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.interest.domain.dto.response.UserInterestAndBookRecommendationsResponse;
import kr.mybrary.interest.domain.dto.response.UserInterestServiceResponse;

public interface InterestService {

    InterestCategoryServiceResponse getInterestCategories();

    UserInterestServiceResponse getUserInterests(String loginId);

    UserInterestServiceResponse updateUserInterests(UserInterestUpdateServiceRequest request);

    UserInterestAndBookRecommendationsResponse getInterestsAndBookRecommendations(UserInterestAndBookRecommendationsServiceRequest request);
}
