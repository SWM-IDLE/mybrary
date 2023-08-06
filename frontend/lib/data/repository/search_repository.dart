import 'package:mybrary/data/datasource/search/search_datasource.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';

class SearchRepository {
  final SearchDataSource _searchDataSource = SearchDataSource();

  Future<BookSearchResponseData> getFollower({
    required String requestUrl,
  }) {
    return _searchDataSource.getBookSearchResponse(requestUrl);
  }
}
