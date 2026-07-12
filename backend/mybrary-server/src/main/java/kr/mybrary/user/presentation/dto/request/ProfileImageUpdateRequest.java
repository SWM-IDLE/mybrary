package kr.mybrary.user.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ProfileImageUpdateRequest {

    private MultipartFile profileImage;

}
