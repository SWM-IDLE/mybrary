package kr.mybrary.userservice.user.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Builder
public class ProfileImageUpdateServiceRequest {

    private MultipartFile profileImage;
    private String loginId;
    private String userId;
    private Date date;

    public static ProfileImageUpdateServiceRequest of(MultipartFile profileImage, String loginId, String userId, Date date) {
        return ProfileImageUpdateServiceRequest.builder()
                .profileImage(profileImage)
                .loginId(loginId)
                .userId(userId)
                .date(date)
                .build();
    }

    public static ProfileImageUpdateServiceRequest of(String loginId, String userId) {
        return ProfileImageUpdateServiceRequest.builder()
                .loginId(loginId)
                .userId(userId)
                .build();
    }
}
