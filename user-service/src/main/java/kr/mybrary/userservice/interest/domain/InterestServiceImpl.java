package kr.mybrary.userservice.interest.domain;

import jakarta.validation.constraints.NotNull;
import kr.mybrary.userservice.interest.domain.dto.InterestCategoryMapper;
import kr.mybrary.userservice.interest.domain.dto.UserInterestMapper;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestServiceResponse;
import kr.mybrary.userservice.interest.domain.exception.UserInterestNotFoundException;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
import kr.mybrary.userservice.interest.persistence.repository.UserInterestRepository;
import kr.mybrary.userservice.user.domain.exception.user.UserNotFoundException;
import kr.mybrary.userservice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public InterestCategoryServiceResponse getInterestCategories() {
        return InterestCategoryServiceResponse.builder()
                .interestCategories(getInterestCategoryResponses())
                .build();
    }

    @NotNull
    private List<InterestCategoryResponse> getInterestCategoryResponses() {
        return interestCategoryRepository.findAllWithInterestUsingFetchJoin()
                .stream()
                .map(InterestCategoryMapper.INSTANCE::toInterestCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserInterestServiceResponse getUserInterests(String loginId) {
        return UserInterestServiceResponse.builder()
                .loginId(loginId)
                .userInterests(getInterestResponses(getAllUserInterestsBy(loginId)))
                .build();
    }

    private List<UserInterest> getAllUserInterestsBy(String loginId) {
        return userInterestRepository.findAllByUserWithInterestUsingFetchJoin(
                userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new)
        );
    }

    @NotNull
    private static List<InterestResponse> getInterestResponses(List<UserInterest> userInterests) {
        checkUserInterestExistence(userInterests);
        return userInterests.stream()
                .map(UserInterestMapper.INSTANCE::toInterestResponse)
                .toList();
    }

    private static void checkUserInterestExistence(List<UserInterest> userInterests) {
        if(userInterests.isEmpty()) {
            throw new UserInterestNotFoundException();
        }
    }

}
