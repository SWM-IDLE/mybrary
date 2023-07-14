const baseUrl = "http://3.34.193.42:8000";
const mybraryUrlScheme = "kr.mybrary";

enum API {
  naverLogin,
  kakaoLogin,
  googleLogin,
  getUserProfile,
  getBookService,
  getBookSearchKeyword,
  getBookSearchIsbn,
}

Map<API, String> apiMap = {
  API.naverLogin: "/oauth2/authorization/naver",
  API.kakaoLogin: "/oauth2/authorization/kakao",
  API.googleLogin: "/oauth2/authorization/google",
  API.getUserProfile: "/api/v1/users/profile",
  API.getBookService: "/api/v1",
  API.getBookSearchKeyword: "/api/v1/books/search",
  API.getBookSearchIsbn: "/api/v1/books/search/isbn", // =isbn
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}
