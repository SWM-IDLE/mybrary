package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImageUrlServiceRequest {

    private String userId;
    private String size;

    public static ProfileImageUrlServiceRequest of(String userId, String size) {
        return ProfileImageUrlServiceRequest.builder()
                .userId(userId)
                .size(size)
                .build();
    }
}
