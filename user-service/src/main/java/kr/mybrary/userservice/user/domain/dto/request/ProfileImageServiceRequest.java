package kr.mybrary.userservice.user.domain.dto.request;

import kr.mybrary.userservice.user.presentation.dto.request.ProfileImageUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ProfileImageServiceRequest {

    private MultipartFile profileImage;
    private String loginId;

    public static ProfileImageServiceRequest of(ProfileImageUpdateRequest profileImageUpdateRequest, String loginId) {
        return ProfileImageServiceRequest.builder()
                .profileImage(profileImageUpdateRequest.getProfileImage())
                .loginId(loginId)
                .build();
    }
}
