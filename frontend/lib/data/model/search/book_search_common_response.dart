import 'package:mybrary/data/model/search/book_completed_status_response.dart';
import 'package:mybrary/data/model/search/book_interest_status_response.dart';
import 'package:mybrary/data/model/search/book_registered_status_response.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';

class BookSearchCommonResponse {
  BookSearchDetailResponseData bookSearchDetailResponseData;
  BookInterestStatusResponseData bookInterestStatusResponseData;
  BookRegisteredStatusResponseData bookRegisteredStatusResponseData;
  BookCompletedStatusResponseData bookCompletedStatusResponseData;

  BookSearchCommonResponse({
    required this.bookSearchDetailResponseData,
    required this.bookInterestStatusResponseData,
    required this.bookRegisteredStatusResponseData,
    required this.bookCompletedStatusResponseData,
  });
}
