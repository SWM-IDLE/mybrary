import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/my_books_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class MyBookCommonData {
  ProfileResponseData profileResponseData;
  List<MyBooksResponseData> myBooksResponseData;
  List<BookListResponseData> interestBooksResponseData;

  MyBookCommonData({
    required this.profileResponseData,
    required this.myBooksResponseData,
    required this.interestBooksResponseData,
  });
}
