import 'package:mybrary/data/datasource/home/home_datasource.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';

class HomeRepository {
  final HomeDataSource _homeDataSource = HomeDataSource();

  Future<TodayRegisteredBookCountResponseData> getTodayRegisteredBookCount() {
    return _homeDataSource.getTodayRegisteredBookCount();
  }

  Future<BookListByCategoryResponseData> getBookListByCategory({
    required String type,
    int? page,
    int? categoryId,
  }) async {
    return _homeDataSource.getBookListByCategory(type, page, categoryId);
  }
}
