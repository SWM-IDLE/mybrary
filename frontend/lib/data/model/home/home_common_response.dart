import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';

class HomeCommonData {
  TodayRegisteredBookCountResponseData todayRegisteredBookCountResponseData;
  BookListByCategoryResponseData bookListByCategoryResponseData;
  BookListByCategoryResponseData bookListByGenreNovelData;
  BookListByCategoryResponseData bookListByPsychologyData;
  BookListByCategoryResponseData bookListByTravelData;

  HomeCommonData({
    required this.todayRegisteredBookCountResponseData,
    required this.bookListByCategoryResponseData,
    required this.bookListByGenreNovelData,
    required this.bookListByPsychologyData,
    required this.bookListByTravelData,
  });
}
