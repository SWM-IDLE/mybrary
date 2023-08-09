package kr.mybrary.userservice.user.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoModel {

    private String loginId;
    private String nickname;
    private String profileImageUrl;

}
