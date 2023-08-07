import 'package:mybrary/data/datasource/search/search_datasource.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';

class SearchRepository {
  final SearchDataSource _searchDataSource = SearchDataSource();

  Future<BookSearchResponseData> getBookSearchResponse({
    required String requestUrl,
  }) {
    return _searchDataSource.getBookSearchResponse(requestUrl);
  }

  Future<BookSearchDetailResponseData> getBookSearchDetailResponse({
    required String isbn13,
  }) {
    return _searchDataSource.getBookSearchDetailResponse(isbn13);
  }
}
