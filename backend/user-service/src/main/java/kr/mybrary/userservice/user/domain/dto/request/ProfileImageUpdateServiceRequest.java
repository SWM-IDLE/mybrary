package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileImageUpdateServiceRequest {

    private MultipartFile profileImage;
    private String loginId;
    private String userId;
    private LocalDateTime localDateTime;

    public static ProfileImageUpdateServiceRequest of(MultipartFile profileImage, String loginId, String userId, LocalDateTime localDateTime) {
        return ProfileImageUpdateServiceRequest.builder()
                .profileImage(profileImage)
                .loginId(loginId)
                .userId(userId)
                .localDateTime(localDateTime)
                .build();
    }

    public static ProfileImageUpdateServiceRequest of(String loginId, String userId) {
        return ProfileImageUpdateServiceRequest.builder()
                .loginId(loginId)
                .userId(userId)
                .build();
    }
}
