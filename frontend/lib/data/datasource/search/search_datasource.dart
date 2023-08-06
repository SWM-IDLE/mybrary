import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class SearchDataSource {
  Future<BookSearchResponseData> getBookSearchResponse(
      String requestUrl) async {
    Dio dio = DioService().to();
    final bookSearchResponse = await dio.get(requestUrl);

    log('도서 조회 응답값: $bookSearchResponse');
    final BookSearchResponse result = commonResponseResult(
      bookSearchResponse,
      () => BookSearchResponse(
        status: bookSearchResponse.data['status'],
        message: bookSearchResponse.data['message'],
        data: BookSearchResponseData.fromJson(
          bookSearchResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
