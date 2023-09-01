import 'dart:developer';

import 'package:dio/dio.dart';

const baseUrl = "https://mybrary.kr";
const mybraryUrlScheme = "kr.mybrary";

enum API {
  // oauth
  naverLogin,
  kakaoLogin,
  googleLogin,
  getRefreshToken,
  // user-service
  getUserProfile,
  getUserProfileImage,
  getUserFollowers,
  getUserFollowings,
  getUserFollowStatus,
  getUserInterests,
  getInterestCategories,
  getUserSearch,
  updateUserProfile,
  updateUserProfileImage,
  updateUserFollowing,
  updateUserInterests,
  deleteUserProfileImage,
  deleteUserFollower,
  deleteUserFollowing,
  deleteUserAccount,
  // book-service search
  getBookService,
  getBookSearchKeyword,
  getBookSearchDetail,
  getBookSearchDetailReviews,
  getBookInterestStatus,
  getBookMyBookRegisteredStatus,
  getBookReadCompleteStatus,
  getMyBooks,
  getMyBookDetail,
  getMyBookReview,
  getInterestBooks,
  getTodayRegisteredBookCount,
  getBookListByCategory,
  getBookListByInterest,
  createMyBook,
  createMyBookReview,
  createOrDeleteInterestBook,
  updateMyBookRecord,
  updateMyBookReview,
  deleteMyBook,
  deleteMyBookReview,
}

Map<API, String> apiMap = {
  // oauth
  API.naverLogin: "/user-service/oauth2/authorization/naver",
  API.kakaoLogin: "/user-service/oauth2/authorization/kakao",
  API.googleLogin: "/user-service/oauth2/authorization/google",
  API.getRefreshToken: "/user-service/auth/v1/refresh",
  // user-service
  API.getUserProfile: "/user-service/api/v1/users", // /{userId}/profile",
  API.getUserProfileImage:
      "/user-service/api/v1/users", // /{userId}/profile/image",
  API.getUserFollowers: "/user-service/api/v1/users", // /{userId}/followers",
  API.getUserFollowings: "/user-service/api/v1/users", // /{userId}/followings",
  API.getUserFollowStatus: "/user-service/api/v1/users/follow",
  API.getUserInterests: "/user-service/api/v1/users", // '/{userId}/interests'
  API.getInterestCategories: "/user-service/api/v1/interest-categories",
  API.getUserSearch:
      "/user-service/api/v1/users/search", // ?nickname={nickname}
  API.updateUserProfile: "/user-service/api/v1/users", // /{userId}/profile",
  API.updateUserProfileImage:
      "/user-service/api/v1/users", // /{userId}/profile/image",
  API.updateUserFollowing: "/user-service/api/v1/users/follow",
  API.updateUserInterests:
      "/user-service/api/v1/users", // '/{userId}/interests'
  API.deleteUserProfileImage:
      "/user-service/api/v1/users", // /{userId}/profile/image",
  API.deleteUserFollower: "/user-service/api/v1/users/follower",
  API.deleteUserFollowing: "/user-service/api/v1/users/follow",
  API.deleteUserAccount: "/user-service/api/v1/users/account",
  // book-service
  API.getBookService: "/book-service/api/v1",
  API.getBookSearchKeyword: "/book-service/api/v1/books/search",
  API.getBookSearchDetail: "/book-service/api/v1/books/detail",
  API.getBookSearchDetailReviews:
      "/book-service/api/v1/books", // '/{isbn13}/reviews'
  API.getBookInterestStatus:
      "/book-service/api/v1/books", // '/{isbn13}/interest-status'
  API.getBookMyBookRegisteredStatus:
      "/book-service/api/v1/books", // '/{isbn13}/mybook-registered-status'
  API.getBookReadCompleteStatus:
      "/book-service/api/v1/books", // '/{isbn13}/read-complete-status'
  API.getMyBooks: "/book-service/api/v1/users", // '/{userId}/mybooks'
  API.getMyBookDetail: "/book-service/api/v1/mybooks", // '/{mybookId}'
  API.getMyBookReview: "/book-service/api/v1/mybooks", // '/{mybookId}/review'
  API.getInterestBooks:
      "/book-service/api/v1/books/users", // '/{userId}/interest'
  API.getTodayRegisteredBookCount:
      "/book-service/api/v1/mybooks/today-registration-count",
  API.getBookListByCategory: "/book-service/api/v1/books/recommendations",
  API.getBookListByInterest:
      "/user-service/api/v1/interests/book-recommendations", // ?type={type}
  API.createMyBook: "/book-service/api/v1/mybooks",
  API.createMyBookReview:
      "/book-service/api/v1/mybooks", // '/{mybookId}/reviews'
  API.createOrDeleteInterestBook:
      "/book-service/api/v1/books", // '/{isbn13}/interest'
  API.updateMyBookRecord: "/book-service/api/v1/mybooks", // '/{mybookId}'
  API.updateMyBookReview: "/book-service/api/v1/reviews", // '/{reviewId}'
  API.deleteMyBook: "/book-service/api/v1/mybooks", // '/{mybookId}'
  API.deleteMyBookReview: "/book-service/api/v1/reviews", // '/{reviewId}'
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
