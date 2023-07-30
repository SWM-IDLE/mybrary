package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ProfileImageUpdateServiceRequest {

    private MultipartFile profileImage;
    private String loginId;

    public static ProfileImageUpdateServiceRequest of(MultipartFile profileImage, String loginId) {
        return ProfileImageUpdateServiceRequest.builder()
                .profileImage(profileImage)
                .loginId(loginId)
                .build();
    }
}
