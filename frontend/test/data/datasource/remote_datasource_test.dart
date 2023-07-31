import 'package:flutter_test/flutter_test.dart';
import 'package:mybrary/data/datasource/search/search_datasource.dart';
import 'package:mybrary/data/network/api.dart';

void main() {
  group('book-service의 keyword 검색 API 요청 테스트', () {
    test('정상 키워드 검색 시 정상 응답인지 확인', () async {
      // given
      String keyword = '돈의속성';
      // when
      final result = await SearchDataSource.getBookSearchResponse(
          '${getApi(API.getBookSearchKeyword)}?keyword=$keyword');
      // then
      expect(result.status, '200 OK');
    });

    test('비정상 키워드 검색 시 데이터 응답이 오류인지 확인', () async {
      // given
      String keyword = 'ㅋㅋㅇㅇㅌ';
      // when
      final result = await SearchDataSource.getBookSearchResponse(
          '${getApi(API.getBookSearchKeyword)}?keyword=$keyword');
      // then
      expect(result, Exception('도서 검색 결과가 존재하지 않습니다.'));
    });
  });

  group('book-service의 nextRequestUrl API 요청 테스트', () {
    test('정상 요청 시 정상 응답인지 확인', () async {
      // given
      String nextUrl = '/books/search?keyword=돈의속성&sort=accuracy&page=2';
      // when
      final result = await SearchDataSource.getBookSearchResponse(
          '${getApi(API.getBookService)}/$nextUrl');
      // then
      expect(result.status, '200 OK');
    });
  });
}
