import 'package:mybrary/data/datasource/book/book_datasource.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/my_books_response.dart';

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
}
