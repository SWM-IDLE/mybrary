import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class RemoteDataSource {
  static Future<void> getProfileData(BuildContext context) async {
    var dio = await authDio(context);
    final profileResponse = await dio.get(getApi(API.getUserProfile));

    log('profile Get 요청 응답값: $profileResponse');
  }

  static Future<BookSearchResponse> getBookSearchResponse(
      String requestUrl) async {
    final dio = Dio();
    final bookSearchResponse = await dio.get(requestUrl);

    try {
      switch (bookSearchResponse.statusCode) {
        case 200:
          final BookSearchResponse bookSearchResult = BookSearchResponse(
            status: bookSearchResponse.data['status'],
            message: bookSearchResponse.data['message'],
            data: BookSearchResponseData.fromJson(
                bookSearchResponse.data['data']),
          );
          return bookSearchResult;
        case 404:
          log('ERROR: 서버에 404 에러가 있습니다.');
          return bookSearchResponse.data;
        default:
          log('ERROR: 서버의 API 호출에 실패했습니다.');
          throw Exception('서버의 API 호출에 실패했습니다.');
      }
    } on DioException catch (error) {
      if (error.response != null) {
        throw Exception('${error.response!.data['errorMessage']}');
      }
      throw Exception('서버 요청에 실패했습니다.');
    }
  }
}