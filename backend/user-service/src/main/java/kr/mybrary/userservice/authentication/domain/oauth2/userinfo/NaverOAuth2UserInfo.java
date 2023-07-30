package kr.mybrary.userservice.authentication.domain.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = getResponse();

        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = getResponse();

        if (response == null) {
            return null;
        }

        return (String) response.get("nickname");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> response = getResponse();

        if (response == null) {
            return null;
        }

        return (String) response.get("profile_image");
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = getResponse();

        if (response == null) {
            return null;
        }

        return (String) response.get("email");
    }

    private Map<String, Object> getResponse() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return response;
    }


}
