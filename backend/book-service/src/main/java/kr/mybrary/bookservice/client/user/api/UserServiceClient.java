package kr.mybrary.bookservice.client.user.api;

import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user-service/api/v1/users/info")
    UserInfoServiceResponse getUsersInfo(@RequestBody UserInfoRequest userInfoRequest);

}
