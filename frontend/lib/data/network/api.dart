import 'dart:developer';

import 'package:dio/dio.dart';

const baseUrl = "http://3.38.185.10:8003";
const mybraryUrlScheme = "kr.mybrary";

enum API {
  // oauth
  naverLogin,
  kakaoLogin,
  googleLogin,
  // user-service
  getUserProfile,
  getUserProfileImage,
  editUserProfile,
  editUserProfileImage,
  deleteUserProfileImage,
  // book-service
  getBookService,
  getBookSearchKeyword,
  getBookSearchIsbn,
}

Map<API, String> apiMap = {
  // oauth
  API.naverLogin: "/oauth2/authorization/naver",
  API.kakaoLogin: "/oauth2/authorization/kakao",
  API.googleLogin: "/oauth2/authorization/google",
  // user-service
  API.getUserProfile: "/api/v1/users/profile",
  API.getUserProfileImage: "/api/v1/users/profile/image",
  API.editUserProfile: "/api/v1/users/profile",
  API.editUserProfileImage: "/api/v1/users/profile/image",
  API.deleteUserProfileImage: "/api/v1/users/profile/image",
  // book-service
  API.getBookService: "/api/v1",
  API.getBookSearchKeyword: "/api/v1/books/search",
  API.getBookSearchIsbn: "/api/v1/books/search/isbn",
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}

commonResponseResult(
  Response<dynamic> response,
  Function successCallback,
) {
  try {
    switch (response.statusCode) {
      case 200:
        return successCallback();
      case 404:
        log('ERROR: 서버에 404 에러가 있습니다.');
        return response.data;
      default:
        log('ERROR: 서버의 API 호출에 실패했습니다.');
        throw Exception('서버의 API 호출에 실패했습니다.');
    }
  } on DioException catch (error) {
    print(error);
    if (error.response != null) {
      throw Exception('${error.response!.data['errorMessage']}');
    }
    throw Exception('서버 요청에 실패했습니다.');
  }
}
