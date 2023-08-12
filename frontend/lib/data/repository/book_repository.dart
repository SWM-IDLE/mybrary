import 'package:mybrary/data/datasource/book/book_datasource.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
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
  }) {
    return _bookDataSource.getMyBooks(userId);
  }

  Future<CommonResponse> createMyBook({
    required String userId,
    required String isbn13,
  }) {
    return _bookDataSource.createMyBook(userId, isbn13);
  }

  Future<MyBookDetailResponseData> getMyBookDetail({
    required String userId,
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookDetail(userId, myBookId);
  }

  Future<MyBookReviewResponseData?> getMyBookReview({
    required int myBookId,
  }) {
    return _bookDataSource.getMyBookReview(myBookId);
  }
}
