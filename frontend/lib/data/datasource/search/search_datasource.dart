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

    log('도서 단순 상세 조회 응답값: $bookSearchDetailResponse');
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

  Future<BookSearchDetailResponseData> getBookSearchDetailAndSaveBookResponse(
    String userId,
    String isbn13,
    String? isbn10,
  ) async {
    Dio dio = DioService().to();
    final bookSearchDetailAndSaveBookResponse = await dio.get(
      '${getBookServiceApi(API.getBookSearchDetailAndSaveBook)}?isbn13=$isbn13${isbn10 != null ? '&isbn10=$isbn10' : ''}',
      options: Options(headers: {'User-Id': userId}),
    );

    log('도서 상세 조회 응답값: $bookSearchDetailAndSaveBookResponse');
    final BookSearchDetailResponse result = commonResponseResult(
      bookSearchDetailAndSaveBookResponse,
      () => BookSearchDetailResponse(
        status: bookSearchDetailAndSaveBookResponse.data['status'],
        message: bookSearchDetailAndSaveBookResponse.data['message'],
        data: BookSearchDetailResponseData.fromJson(
          bookSearchDetailAndSaveBookResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
