import 'package:mybrary/data/datasource/book/book_datasource.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';

class BookRepository {
  final BookDataSource _searchDataSource = BookDataSource();

  Future<InterestBookResponseData> registerInterestBook({
    required String userId,
    required String isbn13,
  }) {
    return _searchDataSource.registerInterestBook(userId, isbn13);
  }
}
