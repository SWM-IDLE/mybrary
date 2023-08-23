import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/book/book_datasource.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_record_reponse.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/common/common_response.dart';

class BookRepository {
  final BookDataSource _bookDataSource = BookDataSource();

  Future<List<BookListResponseData>> getInterestBooks({
    required BuildContext context,
    required String userId,
    String? order,
  }) {
    return _bookDataSource.getInterestBooks(context, userId, order);
  }

  Future<InterestBookResponseData> createOrDeleteInterestBook({
    required BuildContext context,
    required String userId,
    required String isbn13,
  }) {
    return _bookDataSource.createOrDeleteInterestBook(context, userId, isbn13);
  }

  Future<List<MyBooksResponseData>> getMyBooks({
    required BuildContext context,
    required String userId,
    required String order,
    required String readStatus,
  }) {
    return _bookDataSource.getMyBooks(context, userId, order, readStatus);
  }

  Future<CommonResponse> createMyBook({
    required BuildContext context,
    required String userId,
    required String isbn13,
  }) {
    return _bookDataSource.createMyBook(context, userId, isbn13);
  }

  Future<CommonResponse> deleteMyBook({
    required BuildContext context,
    required String userId,
    required int myBookId,
  }) {
    return _bookDataSource.deleteMyBook(context, userId, myBookId);
  }

  Future<MyBookDetailResponseData> getMyBookDetail({
    required BuildContext context,
    required String userId,
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookDetail(context, userId, myBookId);
  }

  Future<MyBookRecordResponseData> updateMyBookRecord({
    required BuildContext context,
    required String userId,
    required int myBookId,
    required MyBookRecordResponseData myBookRecordData,
  }) {
    return _bookDataSource.updateMyBookRecord(
      context,
      userId,
      myBookId,
      myBookRecordData,
    );
  }

  Future<MyBookReviewResponseData?> getMyBookReview({
    required BuildContext context,
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookReview(context, myBookId);
  }

  Future<CommonResponse> createMyBookReview({
    required BuildContext context,
    required String userId,
    required int myBookId,
    required String content,
    required double starRating,
  }) {
    return _bookDataSource.createMyBookReview(
      context,
      userId,
      myBookId,
      content,
      starRating,
    );
  }

  Future<MyBookReviewUpdateResponseData> updateMyBookReview({
    required BuildContext context,
    required String userId,
    required int reviewId,
    required String content,
    required double starRating,
  }) {
    return _bookDataSource.updateMyBookReview(
      context,
      userId,
      reviewId,
      content,
      starRating,
    );
  }

  Future<CommonResponse> deleteMyBookReview({
    required BuildContext context,
    required String userId,
    required int reviewId,
  }) {
    return _bookDataSource.deleteMyBookReview(context, userId, reviewId);
  }
}
