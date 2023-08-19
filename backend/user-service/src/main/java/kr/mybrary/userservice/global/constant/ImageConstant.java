package kr.mybrary.userservice.global.constant;

public enum ImageConstant {

    DEFAULT_PROFILE_IMAGE("https://mybrary-user-service.s3.ap-northeast-2.amazonaws.com/profile/profileImage/default.jpg"),
    DEFAULT_PROFILE_IMAGE_TINY("https://mybrary-user-service-resized.s3.ap-northeast-2.amazonaws.com/tiny-profile/profileImage/default.jpg"),
    DEFAULT_PROFILE_IMAGE_SMALL("https://mybrary-user-service-resized.s3.ap-northeast-2.amazonaws.com/small-profile/profileImage/default.jpg");

    private final String url;

    ImageConstant(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
