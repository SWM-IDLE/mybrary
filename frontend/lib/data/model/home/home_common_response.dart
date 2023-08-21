import 'package:mybrary/data/model/home/book_list_by_category_response.dart';

class HomeCommonData {
  BookListByCategoryResponseData bookListByGenreNovelData;
  BookListByCategoryResponseData bookListByPsychologyData;
  BookListByCategoryResponseData bookListByTravelData;

  HomeCommonData({
    required this.bookListByGenreNovelData,
    required this.bookListByPsychologyData,
    required this.bookListByTravelData,
  });
}
