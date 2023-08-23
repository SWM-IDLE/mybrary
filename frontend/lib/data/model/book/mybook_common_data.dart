import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class MyBookCommonData {
  ProfileResponseData profileResponseData;
  ProfileImageResponseData profileImageResponseData;
  List<MyBooksResponseData> myBooksResponseData;
  List<MyBooksResponseData> completedBooksResponseData;
  List<BookListResponseData> interestBooksResponseData;

  MyBookCommonData({
    required this.profileResponseData,
    required this.profileImageResponseData,
    required this.myBooksResponseData,
    required this.completedBooksResponseData,
    required this.interestBooksResponseData,
  });
}

class MyBookDetailCommonData {
  MyBookDetailResponseData myBooksResponseData;
  MyBookReviewResponseData? myBookReviewResponseData;

  MyBookDetailCommonData({
    required this.myBooksResponseData,
    required this.myBookReviewResponseData,
  });
}
