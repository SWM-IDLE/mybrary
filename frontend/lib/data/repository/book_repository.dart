import 'package:flutter/cupertino.dart';
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
    required String userId,
    String? order,
  }) {
    return _bookDataSource.getInterestBooks(userId, order);
  }

  Future<InterestBookResponseData> createOrDeleteInterestBook({
    required String userId,
    required String isbn13,
  }) {
    return _bookDataSource.createOrDeleteInterestBook(userId, isbn13);
  }

  Future<List<MyBooksResponseData>> getMyBooks({
    required String userId,
    required String order,
    required String readStatus,
  }) {
    return _bookDataSource.getMyBooks(userId, order, readStatus);
  }

  Future<CommonResponse> createMyBook({
    required BuildContext context,
    required String userId,
    required String isbn13,
  }) {
    return _bookDataSource.createMyBook(context, userId, isbn13);
  }

  Future<CommonResponse> deleteMyBook({
    required String userId,
    required int myBookId,
  }) {
    return _bookDataSource.deleteMyBook(userId, myBookId);
  }

  Future<MyBookDetailResponseData> getMyBookDetail({
    required String userId,
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookDetail(userId, myBookId);
  }

  Future<MyBookRecordResponseData> updateMyBookRecord({
    required String userId,
    required int myBookId,
    required MyBookRecordResponseData myBookRecordData,
  }) {
    return _bookDataSource.updateMyBookRecord(
      userId,
      myBookId,
      myBookRecordData,
    );
  }

  Future<MyBookReviewResponseData?> getMyBookReview({
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookReview(myBookId);
  }

  Future<CommonResponse> createMyBookReview({
    required String userId,
    required int myBookId,
    required String content,
    required double starRating,
  }) {
    return _bookDataSource.createMyBookReview(
      userId,
      myBookId,
      content,
      starRating,
    );
  }

  Future<MyBookReviewUpdateResponseData> updateMyBookReview({
    required String userId,
    required int reviewId,
    required String content,
    required double starRating,
  }) {
    return _bookDataSource.updateMyBookReview(
      userId,
      reviewId,
      content,
      starRating,
    );
  }
}
