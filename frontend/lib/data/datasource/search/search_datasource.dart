import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_completed_status_response.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/data/model/search/book_interest_status_response.dart';
import 'package:mybrary/data/model/search/book_registered_status_response.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/model/search/user_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class SearchDataSource {
  Future<BookSearchResponseData> getBookSearchResponse(
    BuildContext context,
    String requestUrl,
  ) async {
    final dio = await authDio(context);
    final bookSearchResponse = await dio.get(requestUrl);

    log('도서 조회 응답값: $bookSearchResponse');
    final BookSearchResponse result = commonResponseResult(
      bookSearchResponse,
      () => BookSearchResponse(
        status: bookSearchResponse.data['status'],
        message: bookSearchResponse.data['message'],
        data: BookSearchResponseData.fromJson(
          bookSearchResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }

  Future<BookSearchDetailResponseData> getBookSearchDetailResponse(
    BuildContext context,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final bookSearchDetailResponse =
        await dio.get('${getApi(API.getBookSearchDetail)}?isbn13=$isbn13');

    log('도서 단순 상세 조회 응답값: $bookSearchDetailResponse');
    final BookSearchDetailResponse result = commonResponseResult(
      bookSearchDetailResponse,
      () => BookSearchDetailResponse(
        status: bookSearchDetailResponse.data['status'],
        message: bookSearchDetailResponse.data['message'],
        data: BookSearchDetailResponseData.fromJson(
          bookSearchDetailResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }

  Future<BookSearchDetailResponseData> getBookSearchDetailAndSaveBookResponse(
    BuildContext context,
    String userId,
    String isbn13,
    String? isbn10,
  ) async {
    final dio = await authDio(context);
    final bookSearchDetailAndSaveBookResponse = await dio.get(
      '${getApi(API.getBookSearchDetail)}?isbn13=$isbn13${isbn10 != null ? '&isbn10=$isbn10' : ''}',
      options: Options(headers: {'User-Id': userId}),
    );

    log('도서 상세 조회 응답값: $bookSearchDetailAndSaveBookResponse');
    final BookSearchDetailResponse result = commonResponseResult(
      bookSearchDetailAndSaveBookResponse,
      () => BookSearchDetailResponse(
        status: bookSearchDetailAndSaveBookResponse.data['status'],
        message: bookSearchDetailAndSaveBookResponse.data['message'],
        data: BookSearchDetailResponseData.fromJson(
          bookSearchDetailAndSaveBookResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }

  Future<BookDetailReviewResponseData> getBookSearchDetailReviews(
    BuildContext context,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final bookSearchDetailReviewsResponse = await dio
        .get('${getApi(API.getBookSearchDetailReviews)}/$isbn13/reviews');

    log('도서 상세 리뷰 응답값: $bookSearchDetailReviewsResponse');
    final BookDetailReviewResponse result = commonResponseResult(
      bookSearchDetailReviewsResponse,
      () => BookDetailReviewResponse(
        status: bookSearchDetailReviewsResponse.data['status'],
        message: bookSearchDetailReviewsResponse.data['message'],
        data: BookDetailReviewResponseData.fromJson(
          bookSearchDetailReviewsResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<BookInterestStatusResponseData> getBookInterestStatusResponse(
    BuildContext context,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final bookInterestStatusResponse = await dio
        .get('${getApi(API.getBookInterestStatus)}/$isbn13/interest-status');

    log('관심 도서 여부 응답값: $bookInterestStatusResponse');
    final BookInterestStatusResponse result = commonResponseResult(
      bookInterestStatusResponse,
      () => BookInterestStatusResponse(
        status: bookInterestStatusResponse.data['status'],
        message: bookInterestStatusResponse.data['message'],
        data: BookInterestStatusResponseData.fromJson(
          bookInterestStatusResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<BookRegisteredStatusResponseData> getBookRegisteredStatusResponse(
    BuildContext context,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final bookRegisteredStatusResponseData = await dio.get(
        '${getApi(API.getBookMyBookRegisteredStatus)}/$isbn13/mybook-registered-status');

    log('등록 도서 여부 응답값: $bookRegisteredStatusResponseData');
    final BookRegisteredStatusResponse result = commonResponseResult(
      bookRegisteredStatusResponseData,
      () => BookRegisteredStatusResponse(
        status: bookRegisteredStatusResponseData.data['status'],
        message: bookRegisteredStatusResponseData.data['message'],
        data: BookRegisteredStatusResponseData.fromJson(
          bookRegisteredStatusResponseData.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<BookCompletedStatusResponseData> getBookCompletedStatusResponse(
    BuildContext context,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final bookCompletedStatusResponse = await dio.get(
        '${getApi(API.getBookInterestStatus)}/$isbn13/read-complete-status');

    log('완독 도서 여부 응답값: $bookCompletedStatusResponse');
    final BookCompletedStatusResponse result = commonResponseResult(
      bookCompletedStatusResponse,
      () => BookCompletedStatusResponse(
        status: bookCompletedStatusResponse.data['status'],
        message: bookCompletedStatusResponse.data['message'],
        data: BookCompletedStatusResponseData.fromJson(
          bookCompletedStatusResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<UserSearchResponseData> getUserSearchResponse(
    BuildContext context,
    String nickname,
  ) async {
    final dio = await authDio(context);
    final userSearchResponse =
        await dio.get('${getApi(API.getUserSearch)}?nickname=$nickname');

    log('사용자 검색 응답값: $userSearchResponse');
    final UserSearchResponse result = commonResponseResult(
      userSearchResponse,
      () => UserSearchResponse(
        status: userSearchResponse.data['status'],
        message: userSearchResponse.data['message'],
        data: UserSearchResponseData.fromJson(
          userSearchResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }
}
