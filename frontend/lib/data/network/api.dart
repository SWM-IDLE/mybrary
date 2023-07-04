const baseUrl = "https://fc68-210-126-14-134.ngrok-free.app";
const mybraryUrlScheme = "kr.mybrary";

enum API {
  naverLogin,
  kakaoLogin,
  googleLogin,
  getUserProfile,
}

Map<API, String> apiMap = {
  API.naverLogin: "/oauth2/authorization/naver",
  API.kakaoLogin: "/oauth2/authorization/kakao",
  API.googleLogin: "/oauth2/authorization/google",
  API.getUserProfile: "/api/v1/users/profile",
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}
