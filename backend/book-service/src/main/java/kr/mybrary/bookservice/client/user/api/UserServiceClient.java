package kr.mybrary.bookservice.client.user.api;

import feign.Headers;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/api/v1/users/info")
    @Headers("Content-Type: application/json")
    UserInfoServiceResponse getUsersInfo(@RequestBody UserInfoRequest userInfoRequest);

}
