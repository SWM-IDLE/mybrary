import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/book_recommendations_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class HomeDataSource {
  Future<TodayRegisteredBookCountResponseData> getTodayRegisteredBookCount(
      BuildContext context) async {
    final dio = await authDio(context);
    final getTodayRegisteredBookCountResponse = await dio.get(
      getApi(API.getTodayRegisteredBookCount),
    );

    log('오늘의 마이북 등록수 조회 응답값: $getTodayRegisteredBookCountResponse');
    final TodayRegisteredBookCountResponse result = commonResponseResult(
      getTodayRegisteredBookCountResponse,
      () => TodayRegisteredBookCountResponse(
        status: getTodayRegisteredBookCountResponse.data['status'],
        message: getTodayRegisteredBookCountResponse.data['message'],
        data: TodayRegisteredBookCountResponseData.fromJson(
          getTodayRegisteredBookCountResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<BookListByCategoryResponseData> getBookListByCategory(
    BuildContext context,
    String type,
    int? page,
    int? categoryId,
  ) async {
    final dio = await authDio(context);
    final getBookListByCategoryResponse = await dio.get(
      '${getApi(API.getBookListByCategory)}?type=$type&page=${page ?? 1}&categoryId=${categoryId ?? 0}',
    );

    log('카테고리 도서 목록 조회 응답값: $getBookListByCategoryResponse');
    final BookListByCategoryResponse result = commonResponseResult(
      getBookListByCategoryResponse,
      () => BookListByCategoryResponse(
        status: getBookListByCategoryResponse.data['status'],
        message: getBookListByCategoryResponse.data['message'],
        data: BookListByCategoryResponseData.fromJson(
          getBookListByCategoryResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<BookRecommendationsResponseData> getBookListByInterest(
    BuildContext context,
    String type,
    String userId,
    int? page,
  ) async {
    final dio = await authDio(context);
    final getBookListByInterestResponse = await dio.get(
      '${getApi(API.getBookListByInterest)}/$type&page=${page ?? 1}',
      options: Options(headers: {'User-Id': userId}),
    );

    log('관심사별 추천 도서 조회 응답값: $getBookListByInterestResponse');
    final BookRecommendationsResponse result = commonResponseResult(
      getBookListByInterestResponse,
      () => BookRecommendationsResponse(
        status: getBookListByInterestResponse.data['status'],
        message: getBookListByInterestResponse.data['message'],
        data: BookRecommendationsResponseData.fromJson(
          getBookListByInterestResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }
}
