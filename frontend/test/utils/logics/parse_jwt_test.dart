import 'package:flutter_test/flutter_test.dart';
import 'package:mybrary/utils/logics/parse_utils.dart';

void main() {
  group('jwt 정상 토큰 파싱 테스트', () {
    test('정상적인 토큰이면, 파싱한 json 반환', () {
      // given
      String token =
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
      // when
      final result = parseJwt(token);
      // then
      expect(result is! Exception, true);
    });
  });

  group('header, payload, signature 중 1개라도 없으면, Exception 반환', () {
    test('header가 없는 경우', () {
      // given
      String token =
          'eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
      // when, then
      expect(() => parseJwt(token), throwsException);
    });

    test('payload 없는 경우', () {
      // given
      String token =
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
      // when, then
      expect(() => parseJwt(token), throwsException);
    });

    test('signature가 없는 경우', () {
      // given
      String token =
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ';
      // when, then
      expect(() => parseJwt(token), throwsException);
    });
  });

  group('payload 또는 토큰값 자체가 올바르지 않으면, Exception 반환', () {
    test('payload 중간이 삭제된 경우', () {
      // given
      String token =
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOi0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
      // when, then
      expect(() => parseJwt(token), throwsException);
    });

    test('토큰값 자체가 이상한 경우', () {
      // given
      String token = 'nR5cCI6IkpXVCJwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
      // when, then
      expect(() => parseJwt(token), throwsException);
    });
  });
}
