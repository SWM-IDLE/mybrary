import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/home/home_datasource.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/book_recommendations_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';

class HomeRepository {
  final HomeDataSource _homeDataSource = HomeDataSource();

  Future<TodayRegisteredBookCountResponseData> getTodayRegisteredBookCount({
    required BuildContext context,
  }) {
    return _homeDataSource.getTodayRegisteredBookCount(context);
  }

  Future<BookListByCategoryResponseData> getBookListByCategory({
    required BuildContext context,
    required String type,
    int? page,
    int? categoryId,
  }) async {
    return _homeDataSource.getBookListByCategory(
      context,
      type,
      page,
      categoryId,
    );
  }

  Future<BookRecommendationsResponseData> getBookListByInterest({
    required BuildContext context,
    required String type,
    required String userId,
    int? page,
  }) async {
    return _homeDataSource.getBookListByInterest(context, type, userId, page);
  }
}
