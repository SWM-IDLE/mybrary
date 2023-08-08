import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class BookDataSource {
  Future<InterestBookResponseData> registerInterestBook(
    String userId,
    String isbn13,
  ) async {
    Dio dio = DioService().to();
    final registerInterestBookResponse = await dio.post(
      '${getBookServiceApi(API.registerInterestBook)}/$isbn13/interest',
      options: Options(headers: {'User-Id': userId}),
    );

    log('관심 도서 등록 및 삭제 응답값: $registerInterestBookResponse');
    final InterestBookResponse result = commonResponseResult(
      registerInterestBookResponse,
      () => InterestBookResponse(
        status: registerInterestBookResponse.data['status'],
        message: registerInterestBookResponse.data['message'],
        data: InterestBookResponseData.fromJson(
          registerInterestBookResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
