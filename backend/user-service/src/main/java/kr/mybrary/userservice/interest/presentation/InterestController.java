package kr.mybrary.userservice.interest.presentation;

import kr.mybrary.userservice.global.dto.response.SuccessResponse;
import kr.mybrary.userservice.interest.domain.InterestService;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestAndBookRecommendationsServiceRequest;
import kr.mybrary.userservice.interest.domain.dto.request.UserInterestUpdateServiceRequest;
import kr.mybrary.userservice.interest.presentation.dto.request.UserInterestUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/interest-categories")
    public ResponseEntity<SuccessResponse> getInterestCategories() {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "카테고리별 관심사를 모두 조회했습니다.",
                        interestService.getInterestCategories())
        );
    }

    @GetMapping("/users/{userId}/interests")
    public ResponseEntity<SuccessResponse> getUserInterests(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 관심사를 모두 조회했습니다.",
                        interestService.getUserInterests(userId))
        );
    }

    @PutMapping("/users/{userId}/interests")
    public ResponseEntity<SuccessResponse> updateUserInterests(
            @PathVariable("userId") String userId,
            @RequestHeader("USER-ID") String loginId,
            @RequestBody UserInterestUpdateRequest request) {

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 관심사를 수정했습니다.",
                        interestService.updateUserInterests(
                                UserInterestUpdateServiceRequest.of(userId, loginId, request.getInterestRequests())))
        );
    }

    @GetMapping("/interests/book-recommendations/{type}")
    public ResponseEntity<SuccessResponse> getInterestsAndBookRecommendations(
            @RequestHeader("USER-ID") String userId,
            @PathVariable String type,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        UserInterestAndBookRecommendationsServiceRequest request =
                UserInterestAndBookRecommendationsServiceRequest.of(userId, type, page);

        return ResponseEntity.ok().body(
                SuccessResponse.of(HttpStatus.OK.toString(), "사용자의 모든 관심사와 그 중 하나의 관심사에 대한 추천 도서를 조회했습니다.",
                        interestService.getInterestsAndBookRecommendations(request))
        );
    }
}