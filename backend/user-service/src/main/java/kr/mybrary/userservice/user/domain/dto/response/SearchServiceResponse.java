package kr.mybrary.userservice.user.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchServiceResponse {

    private List<SearchedUser> searchedUsers;

    @Getter
    @Builder
    public static class SearchedUser {
        private String userId;
        private String nickname;
        private String profileImageUrl;
    }

}
