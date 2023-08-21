package kr.mybrary.userservice.interest.domain;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import kr.mybrary.userservice.client.book.api.BookServiceClient;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.InterestCategoryMapper;
import kr.mybrary.userservice.interest.domain.dto.UserInterestMapper;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestAndBookRecommendationsServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestCategoryServiceResponse;
import kr.mybrary.userservice.interest.domain.dto.response.InterestResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestAndBookRecommendationsResponse;
import kr.mybrary.userservice.interest.domain.dto.response.UserInterestServiceResponse;
import kr.mybrary.userservice.interest.domain.exception.DuplicateUserInterestUpdateRequestException;
import kr.mybrary.userservice.interest.domain.exception.InterestNotFoundException;
import kr.mybrary.userservice.interest.domain.exception.UserInterestUpdateRequestNotAuthenticated;
import kr.mybrary.userservice.interest.domain.exception.UserInterestUpdateRequestSizeExceededException;
import kr.mybrary.userservice.interest.persistence.Interest;
import kr.mybrary.userservice.interest.persistence.UserInterest;
import kr.mybrary.userservice.interest.persistence.repository.InterestCategoryRepository;
import kr.mybrary.userservice.interest.persistence.repository.InterestRepository;
import kr.mybrary.userservice.interest.persistence.repository.UserInterestRepository;
import kr.mybrary.userservice.user.domain.UserService;
import kr.mybrary.userservice.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestCategoryRepository interestCategoryRepository;
    private final UserInterestRepository userInterestRepository;
    private final InterestRepository interestRepository;
    private final UserService userService;
    private final BookServiceClient bookServiceClient;

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
                .userId(loginId)
                .userInterests(getInterestResponses(getAllUserInterestsBy(loginId)))
                .build();
    }

    private List<UserInterest> getAllUserInterestsBy(String loginId) {
        return userInterestRepository.findAllByUserWithInterestUsingFetchJoin(
                userService.getUserResponse(loginId).getUser());
    }

    @NotNull
    private static List<InterestResponse> getInterestResponses(List<UserInterest> userInterests) {
        return userInterests.stream()
                .map(UserInterestMapper.INSTANCE::toInterestResponse)
                .toList();
    }

    @Override
    public UserInterestServiceResponse updateUserInterests(UserInterestUpdateServiceRequest request) {
        checkUserInterestUpdateRequestAuthentication(request);
        checkUserInterestUpdateRequestSize(request);
        checkDuplicatedUserInterestUpdateRequest(request);

        User user = userService.getUserResponse(request.getLoginId()).getUser();
        deleteOriginalUserInterests(user);
        saveRequestedUserInterests(request.getInterestIds(), user);
        return getUserInterests(request.getLoginId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserInterestAndBookRecommendationsResponse getInterestsAndBookRecommendations(
            UserInterestAndBookRecommendationsServiceRequest request) {

        User user = userService.getUserResponse(request.getLoginId()).getUser();

        List<Interest> Interests = userInterestRepository.findAllByUserWithInterestUsingFetchJoin(user).stream()
                .map(UserInterest::getInterest)
                .toList();

        if (Interests.isEmpty()) {
            return UserInterestAndBookRecommendationsResponse.of(List.of(), List.of());
        }

        BookRecommendationsServiceResponse bookRecommendations = bookServiceClient.getBookListByCategoryId(
                request.getType(), Interests.get(0).getCode(), request.getPage());

        return UserInterestAndBookRecommendationsResponse.of(Interests, bookRecommendations.getData().getBooks());
    }

    private void checkUserInterestUpdateRequestAuthentication(UserInterestUpdateServiceRequest request) {
        if(!request.getUserId().equals(request.getLoginId())) {
            throw new UserInterestUpdateRequestNotAuthenticated();
        }
    }

    private void checkUserInterestUpdateRequestSize(UserInterestUpdateServiceRequest request) {
        if(request.getInterestIds().size() > 3) {
            throw new UserInterestUpdateRequestSizeExceededException();
        }
    }

    private void checkDuplicatedUserInterestUpdateRequest(UserInterestUpdateServiceRequest request) {
        if(request.getInterestIds().stream().distinct().count() != request.getInterestIds().size()) {
            throw new DuplicateUserInterestUpdateRequestException();
        }
    }

    private void deleteOriginalUserInterests(User user) {
        userInterestRepository.deleteAllByUser(user);
        userInterestRepository.flush();
    }

    private void saveRequestedUserInterests(List<Long> interestIds, User user) {
        interestIds.stream()
                .map(interestId -> UserInterest.builder()
                        .user(user)
                        .interest(getInterest(interestId))
                        .build())
                .forEach(userInterestRepository::save);
    }

    private Interest getInterest(Long interestId) {
        return interestRepository.findById(interestId).orElseThrow(InterestNotFoundException::new);
    }

}
