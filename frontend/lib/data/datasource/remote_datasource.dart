import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

const ngrokTestApi =
    'https://e336-221-148-248-129.ngrok-free.app/book-service/api/v1/books/search';

class RemoteDataSource {
  Future<void> getProfileData(BuildContext context) async {
    var dio = await authDio(context);
    // profile 응답을 위한 API Get 요청
    final profileResponse = await dio.get(getApi(API.getUserProfile));

    log('profile Get 요청 응답값: $profileResponse');
  }

  Future<Map<String, dynamic>> getBookSearchKeywordData(String keyword) async {
    final dio = Dio();
    final bookSearchKeywordResponse =
        await dio.get('$ngrokTestApi?keyword=$keyword');

    if (bookSearchKeywordResponse.statusCode == 200) {
      final bookSearchKeywordResult = bookSearchKeywordResponse.data;
      // final List<dynamic> bookSearchKeywordList = bookSearchKeywordResponse
      //     .data['data']['bookSearchResult'] as List<dynamic>;
      //
      // final searchBookDataResults =
      //     convertSearchBookDataList(bookSearchKeywordList);

      return bookSearchKeywordResult;
    } else if (bookSearchKeywordResponse.statusCode == 404) {
      return bookSearchKeywordResponse.data;
    } else {
      log('ERROR: 서버의 API 호출에 실패했습니다.');
      throw Exception('서버의 API 호출에 실패했습니다.');
    }
  }
}
