import 'package:flutter_test/flutter_test.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';

void main() {
  group('book-service의 keyword 검색 API 요청 테스트', () {
    test('정상 키워드 검색 시 정상 응답인지 확인', () async {
      // given
      String keyword = '돈의속성';
      // when
      final result = await RemoteDataSource.getBookSearchKeywordResponse(
          BookSearchRequestType.searchKeyword, keyword);
      // then
      expect(result.status, '200 OK');
    });

    test('비정상 키워드 검색 시 데이터 응답이 오류인지 확인', () async {
      // given
      String keyword = 'ㅋㅋㅇㅇㅌ';
      // when
      final result = await RemoteDataSource.getBookSearchKeywordResponse(
          BookSearchRequestType.searchKeyword, keyword);
      // then
      expect(result, Exception('도서 검색 결과가 존재하지 않습니다.'));
    });
  });

  group('book-service의 nextRequestUrl API 요청 테스트', () {
    test('정상 요청 시 정상 응답인지 확인', () async {
      // given
      String nextUrl = '/books/search?keyword=돈의속성&sort=accuracy&page=2';
      // when
      final result = await RemoteDataSource.getBookSearchKeywordResponse(
          BookSearchRequestType.searchNextUrl, nextUrl);
      // then
      expect(result.status, '200 OK');
    });

    test('비정상 키워드 검색 시 데이터 응답이 오류인지 확인', () async {
      // given
      String nextUrl = 'ㅋㅋㅇㅇㅌ';
      // when
      final result = await RemoteDataSource.getBookSearchKeywordResponse(
          BookSearchRequestType.searchNextUrl, nextUrl);
      // then
      expect(result, Exception('도서 검색 결과가 존재하지 않습니다.'));
    });
  });
}
