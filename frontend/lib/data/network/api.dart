const baseUrl = "https://bb06-121-131-134-220.ngrok-free.app";
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
  API.getBookService: "/book-service/api/v1",
  API.getBookSearchKeyword: "/book-service/api/v1/books/search",
  API.getBookSearchIsbn: "/book-service/api/v1/books/search/isbn", // =isbn
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}
