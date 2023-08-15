import 'dart:developer';

import 'package:dio/dio.dart';

const mybraryUrl = "http://mybrary.kr";
const mybraryUrlScheme = "kr.mybrary";
const baseUrl = "$mybraryUrl:8003";
const bookServiceUrl = "$mybraryUrl:8004";

enum API {
  // oauth
  naverLogin,
  kakaoLogin,
  googleLogin,
  // user-service
  getUserProfile,
  getUserProfileImage,
  getUserFollowers,
  getUserFollowings,
  getUserInterests,
  getInterestCategories,
  updateUserProfile,
  updateUserProfileImage,
  updateUserFollowing,
  updateUserInterests,
  deleteUserProfileImage,
  deleteUserFollower,
  deleteUserFollowing,
  // book-service search
  getBookService,
  getBookSearchKeyword,
  getBookSearchDetail,
  getBookSearchDetailReviews,
  getMyBooks,
  getMyBookDetail,
  getMyBookReview,
  getInterestBooks,
  createMyBook,
  createMyBookReview,
  createOrDeleteInterestBook,
  updateMyBookRecord,
  deleteMyBook,
}

Map<API, String> apiMap = {
  // oauth
  API.naverLogin: "/oauth2/authorization/naver",
  API.kakaoLogin: "/oauth2/authorization/kakao",
  API.googleLogin: "/oauth2/authorization/google",
  // user-service
  API.getUserProfile: "/api/v1/users", // /{userId}/profile",
  API.getUserProfileImage: "/api/v1/users", // /{userId}/profile/image",
  API.getUserFollowers: "/api/v1/users", // /{userId}/followers",
  API.getUserFollowings: "/api/v1/users", // /{userId}/followings",
  API.getUserInterests: "/api/v1/users", // '/{userId}/interests'
  API.getInterestCategories: "/api/v1/interest-categories",
  API.updateUserProfile: "/api/v1/users", // /{userId}/profile",
  API.updateUserProfileImage: "/api/v1/users", // /{userId}/profile/image",
  API.updateUserFollowing: "/api/v1/users/follow",
  API.updateUserInterests: "/api/v1/users", // '/{userId}/interests'
  API.deleteUserProfileImage: "/api/v1/users", // /{userId}/profile/image",
  API.deleteUserFollower: "/api/v1/users/follower",
  API.deleteUserFollowing: "/api/v1/users/follow",
  // book-service
  API.getBookService: "/api/v1",
  API.getBookSearchKeyword: "/api/v1/books/search",
  API.getBookSearchDetail: "/api/v1/books/detail",
  API.getBookSearchDetailReviews: "/api/v1/books", // '/{isbn13}/reviews'
  API.getMyBooks: "/api/v1/users", // '/{userId}/mybooks'
  API.getMyBookDetail: "/api/v1/mybooks", // '/{mybookId}'
  API.getMyBookReview: "/api/v1/mybooks", // '/{mybookId}/review'
  API.getInterestBooks: "/api/v1/books/users", // '/{userId}/interest'
  API.createMyBook: "/api/v1/mybooks",
  API.createMyBookReview: "/api/v1/mybooks", // '/{mybookId}/reviews'
  API.createOrDeleteInterestBook: "/api/v1/books", // '/{isbn13}/interest'
  API.updateMyBookRecord: "/api/v1/mybooks", // '/{mybookId}'
  API.deleteMyBook: "/api/v1/mybooks", // '/{mybookId}'
};

String getApi(API apiType) {
  String api = baseUrl;
  api += apiMap[apiType]!;
  return api;
}

String getBookServiceApi(API apiType) {
  String api = bookServiceUrl;
  api += apiMap[apiType]!;
  return api;
}

commonResponseResult(
  Response<dynamic> response,
  Function successCallback,
) {
  try {
    switch (response.statusCode) {
      case 200 || 201:
        return successCallback();
      case 404:
        log('ERROR: 서버에 404 에러가 있습니다.');
        return response.data;
      default:
        log('ERROR: 서버의 API 호출에 실패했습니다.');
        throw Exception('서버의 API 호출에 실패했습니다.');
    }
  } on DioException catch (error) {
    if (error.response != null) {
      throw Exception('${error.response!.data['errorMessage']}');
    }
    throw Exception('서버 요청에 실패했습니다.');
  }
}
