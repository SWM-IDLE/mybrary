package kr.mybrary.bookservice.client.user.api;

import feign.Headers;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse.UserInfoList;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    String DEFAULT_PROFILE_IMAGE_URL = "https://mybrary-user-service-resized.s3.ap-northeast-2.amazonaws.com/tiny-profile/profileImage/default.jpg";
    
    @PostMapping("/api/v1/users/info")
    @Headers("Content-Type: application/json")
    @Retry(name = "userServiceRetryConfig", fallbackMethod = "getUsersInfoFallback")
    @CircuitBreaker(name = "userServiceCircuitBreakerConfig", fallbackMethod = "getUsersInfoFallback")
    UserInfoServiceResponse getUsersInfo(@RequestBody UserInfoRequest userInfoRequest);

    default UserInfoServiceResponse getUsersInfoFallback(UserInfoRequest userInfoRequest, Exception ex) {
        return UserInfoServiceResponse.builder()
                .data(makeTemporaryResponse(userInfoRequest))
                .build();
    }

    private UserInfoList makeTemporaryResponse(UserInfoRequest userInfoRequest) {
        return UserInfoList.builder()
                .userInfoElements(userInfoRequest.getUserIds().stream()
                        .map(userId -> UserInfoServiceResponse.UserInfo.builder()
                                .userId(userId)
                                .nickname(makeTemporaryNickname(userId))
                                .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
                                .build()
                        ).toList()
        ).build();
    }

    @NotNull
    private String makeTemporaryNickname(String userId) {
        return "user_" + userId.substring(0, 5).toLowerCase();
    }
}
