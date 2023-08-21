import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/search/search_datasource.dart';
import 'package:mybrary/data/model/search/book_completed_status_response.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/data/model/search/book_interest_status_response.dart';
import 'package:mybrary/data/model/search/book_registered_status_response.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/model/search/user_search_response.dart';

class SearchRepository {
  final SearchDataSource _searchDataSource = SearchDataSource();

  Future<BookSearchResponseData> getBookSearchResponse({
    required BuildContext context,
    required String requestUrl,
  }) {
    return _searchDataSource.getBookSearchResponse(context, requestUrl);
  }

  Future<BookSearchDetailResponseData> getBookSearchDetailResponse({
    required BuildContext context,
    required String isbn13,
  }) {
    return _searchDataSource.getBookSearchDetailResponse(context, isbn13);
  }

  Future<BookSearchDetailResponseData> getBookSearchDetailAndSaveBookResponse({
    required BuildContext context,
    required String userId,
    required String isbn13,
    String? isbn10,
  }) {
    return _searchDataSource.getBookSearchDetailAndSaveBookResponse(
      context,
      userId,
      isbn13,
      isbn10,
    );
  }

  Future<BookDetailReviewResponseData> getBookSearchDetailReviews({
    required BuildContext context,
    required String isbn13,
  }) {
    return _searchDataSource.getBookSearchDetailReviews(context, isbn13);
  }

  Future<BookInterestStatusResponseData> getBookInterestStatusResponse({
    required BuildContext context,
    required String isbn13,
  }) {
    return _searchDataSource.getBookInterestStatusResponse(context, isbn13);
  }

  Future<BookRegisteredStatusResponseData> getBookRegisteredStatusResponse({
    required BuildContext context,
    required String isbn13,
  }) {
    return _searchDataSource.getBookRegisteredStatusResponse(context, isbn13);
  }

  Future<BookCompletedStatusResponseData> getBookCompletedStatusResponse({
    required BuildContext context,
    required String isbn13,
  }) {
    return _searchDataSource.getBookCompletedStatusResponse(context, isbn13);
  }

  Future<UserSearchResponseData> getUserSearchResponse({
    required BuildContext context,
    required String nickname,
  }) {
    return _searchDataSource.getUserSearchResponse(context, nickname);
  }
}
