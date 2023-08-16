import 'package:mybrary/data/datasource/home/home_datasource.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';

class HomeRepository {
  final HomeDataSource _homeDataSource = HomeDataSource();

  Future<TodayRegisteredBookCountResponseData>
      getTodayRegisteredBookCount() async {
    return _homeDataSource.getTodayRegisteredBookCount();
  }
}
