import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
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

  Future<BookSearchDetailResponseData> getBookSearchDetailResponse(
      String isbn13) async {
    Dio dio = DioService().to();
    final bookSearchDetailResponse = await dio
        .get('${getBookServiceApi(API.getBookSearchDetail)}?isbn=$isbn13');

    log('도서 상세 조회 응답값: $bookSearchDetailResponse');
    final BookSearchDetailResponse result = commonResponseResult(
      bookSearchDetailResponse,
      () => BookSearchDetailResponse(
        status: bookSearchDetailResponse.data['status'],
        message: bookSearchDetailResponse.data['message'],
        data: BookSearchDetailResponseData.fromJson(
          bookSearchDetailResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
