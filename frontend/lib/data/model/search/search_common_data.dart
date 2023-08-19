import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/model/search/user_search_response.dart';

class SearchCommonData {
  BookSearchResponseData bookSearchResponseData;
  UserSearchResponseData userSearchResponseData;

  SearchCommonData({
    required this.bookSearchResponseData,
    required this.userSearchResponseData,
  });
}
