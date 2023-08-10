import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class BookDataSource {
  Future<InterestBookResponseData> createOrDeleteInterestBook(
    String userId,
    String isbn13,
  ) async {
    Dio dio = DioService().to();
    final createOrDeleteInterestBookResponse = await dio.post(
      '${getBookServiceApi(API.createOrDeleteInterestBook)}/$isbn13/interest',
      options: Options(headers: {'User-Id': userId}),
    );

    log('관심 도서 등록 및 삭제 응답값: $createOrDeleteInterestBookResponse');
    final InterestBookResponse result = commonResponseResult(
      createOrDeleteInterestBookResponse,
      () => InterestBookResponse(
        status: createOrDeleteInterestBookResponse.data['status'],
        message: createOrDeleteInterestBookResponse.data['message'],
        data: InterestBookResponseData.fromJson(
          createOrDeleteInterestBookResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
