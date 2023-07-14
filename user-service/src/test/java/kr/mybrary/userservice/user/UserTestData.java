package kr.mybrary.userservice.user;

import java.io.FileInputStream;
import kr.mybrary.userservice.user.domain.dto.request.ProfileImageUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.ProfileUpdateServiceRequest;
import kr.mybrary.userservice.user.domain.dto.request.SignUpServiceRequest;
import org.springframework.mock.web.MockMultipartFile;

public class UserTestData {

    public static SignUpServiceRequest createSignUpServiceRequest() {
        return SignUpServiceRequest.builder()
                .loginId("loginId")
                .password("password123!")
                .nickname("nickname")
                .email("email@mail.com")
                .build();
    }

    public static ProfileUpdateServiceRequest createProfileUpdateServiceRequest() {
        return ProfileUpdateServiceRequest.builder()
                .loginId("loginId")
                .nickname("updated_nickname")
                .introduction("updated_introduction")
                .build();
    }

    public static ProfileImageUpdateServiceRequest createProfileImageUpdateServiceRequest()
            throws Exception {
        return ProfileImageUpdateServiceRequest.builder()
                .loginId("loginId")
                .profileImage(new MockMultipartFile("profileImage", "user1.png", "image/jpg",
                        new FileInputStream("src/test/resources/images/Image1MB.jpg")))
                .build();
    }

    public static ProfileImageUpdateServiceRequest createProfileImageUpdateServiceRequestWithEmptyFile() {
        return ProfileImageUpdateServiceRequest.builder()
                .loginId("loginId")
                .profileImage(new MockMultipartFile("profileImage", "user1.png", "image/jpg", new byte[0]))
                .build();
    }

    public static ProfileImageUpdateServiceRequest createProfileImageUpdateServiceRequestOver5MB()
            throws Exception {
        return ProfileImageUpdateServiceRequest.builder()
                .loginId("loginId")
                .profileImage(new MockMultipartFile("profileImage", "user1.png", "image/jpeg",
                        new FileInputStream("src/test/resources/images/Image6MB.jpeg")))
                .build();
    }
}
