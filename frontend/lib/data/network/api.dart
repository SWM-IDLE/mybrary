const baseUrl = "https://c8be-211-47-82-177.ngrok-free.app";
const mybraryUrlScheme = "kr.mybrary";

enum API {
  naverLogin,
  kakaoLogin,
  googleLogin,
}

Map<API, String> apiMap = {
  API.naverLogin: "/oauth2/authorization/naver",
  API.kakaoLogin: "/oauth2/authorization/kakao",
  API.googleLogin: "/oauth2/authorization/google",
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}
