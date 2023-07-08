import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:mybrary/data/model/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

const ngrokBaseurl =
    'https://b357-1-236-97-56.ngrok-free.app/book-service/api/v1';
const ngrokTestApi =
    'https://b357-1-236-97-56.ngrok-free.app/book-service/api/v1/books/search';

class RemoteDataSource {
  static Future<void> getProfileData(BuildContext context) async {
    var dio = await authDio(context);
    final profileResponse = await dio.get(getApi(API.getUserProfile));

    log('profile Get 요청 응답값: $profileResponse');
  }

  static Future<BookSearchResponse> getBookSearchKeywordResponse(String keyword,
      {String? nextRequestUrl}) async {
    try {
      final dio = Dio();
      final bookSearchKeywordResponse = nextRequestUrl != null
          ? await dio.get('$ngrokBaseurl$nextRequestUrl')
          : await dio.get('$ngrokTestApi?keyword=$keyword');

      if (bookSearchKeywordResponse.statusCode == 200) {
        final BookSearchResponse bookSearchKeywordResult = BookSearchResponse(
          status: bookSearchKeywordResponse.data['status'],
          message: bookSearchKeywordResponse.data['message'],
          data: BookSearchResponseData.fromJson(
              bookSearchKeywordResponse.data['data']),
        );
        return bookSearchKeywordResult;
      } else if (bookSearchKeywordResponse.statusCode == 404) {
        log('ERROR: 서버에 404 에러가 있습니다.');
        return bookSearchKeywordResponse.data;
      } else {
        log('ERROR: 서버의 API 호출에 실패했습니다.');
        throw Exception('서버의 API 호출에 실패했습니다.');
      }
    } catch (e) {
      return Future.error('ERROR: 서버 자체에 오류가 있습니다. 오류는 $e 입니다.');
    }
  }
}
