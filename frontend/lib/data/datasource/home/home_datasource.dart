import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class HomeDataSource {
  Future<TodayRegisteredBookCountResponseData>
      getTodayRegisteredBookCount() async {
    Dio dio = DioService().to();
    final getTodayRegisteredBookCountResponse = await dio.get(
      getBookServiceApi(API.getTodayRegisteredBookCount),
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
}
