import 'package:mybrary/data/network/api.dart';

enum BookSearchRequestType {
  searchKeyword,
  searchNextUrl,
  searchIsbnScan,
}

class RequestType {
  static String getBookSearchRequestUrl(BookSearchRequestType requestType) {
    String requestUrl;
    switch (requestType) {
      case BookSearchRequestType.searchKeyword:
        requestUrl = getApi(API.getBookSearchKeyword);
        break;
      case BookSearchRequestType.searchNextUrl:
        requestUrl = getApi(API.getBookService);
        break;
      case BookSearchRequestType.searchIsbnScan:
        requestUrl = getApi(API.getBookSearchIsbn);
        break;
    }
    return requestUrl;
  }
}
