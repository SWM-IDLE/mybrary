import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/utils/logics/parse_jwt.dart';

Future<Dio> authDio(BuildContext context) async {
  var dio = Dio();

  const secureStorage = FlutterSecureStorage();

  dio.interceptors.clear();

  dio.interceptors.add(InterceptorsWrapper(onRequest: (options, handler) async {
    // device의 secureStorage에 저장된 AccessToken 불러오기
    final accessToken = await secureStorage.read(key: 'ACCESS_TOKEN');

    final jwtPayload = parseJwt(accessToken!);

    // 현재 날짜의 밀리초를 1000으로 나누어 타임스탬프 반환
    final int todayTimeStamp = DateTime.now().millisecondsSinceEpoch ~/ 1000;
    final int expiredTimeStamp = jwtPayload['exp'];
    final String loginId = jwtPayload['loginId'];
    // 만료 시간이 현재 시간보다 크면 만료되지 않음
    if (expiredTimeStamp >= todayTimeStamp) {
      // AccessToken이 만료되지 않았다면, 요청 헤더에 AccessToken 전달
      options.headers['Authorization'] = 'Bearer $accessToken';
      options.headers['User-Id'] = loginId;
      return handler.next(options);
    }

    // 만료되었다면, DioException 발생
    options.data = {'isExpired': true};
    return handler.reject(DioException(requestOptions: options), true);
  }, onError: (error, handler) async {
    log('토큰 만료에 대한 클라이언트 및 서버 에러입니다.');

    bool isExpired = error.requestOptions.data['isExpired'];
    // 1. AccessToken 만료에 대한 인증 오류 발생
    // AccessToken이 만료되었거나, statusCode가 401 이면 발생
    if (isExpired || error.response?.statusCode == 401) {
      final accessToken = await secureStorage.read(key: 'ACCESS_TOKEN');
      final refreshToken = await secureStorage.read(key: 'REFRESH_TOKEN');

      // 토큰 갱신 요청을 수행할 dio 객체 생성 및 interceptor 설정
      var refreshDio = Dio();

      refreshDio.interceptors.clear();
      refreshDio.interceptors
          .add(InterceptorsWrapper(onError: (error, handler) async {
        // 2. RefreshToken 만료에 대한 인증 오류 발생 (status == 401)
        if (error.response?.statusCode == 401) {
          // 사용자 기기에 저장된 로그인 정보를 삭제 후, 로그인 재 요청
          await secureStorage.deleteAll();

          // 로그인 페이지로 이동
          Navigator.of(context).pushNamedAndRemoveUntil(
              '/signin', (Route<dynamic> route) => false);
        }
        return handler.next(error);
      }));

      // 토큰 갱신을 위한 API 요청 시 만료된 Access/Refresh Token 포함하여 전달
      refreshDio.options.headers['Authorization'] = 'Bearer $accessToken';
      refreshDio.options.headers['Authorization-Refresh'] =
          'Bearer $refreshToken';

      // 토큰 갱신을 위한 API 요청 부분
      // 현재 요청한 API에 새 토큰을 재 요청하기 때문에 requestOptions.path 호출
      final refreshResponse = await refreshDio.get(error.requestOptions.path);
      print(refreshResponse.headers);

      // 요청한 API의 response에서 갱신된 Access/Refresh Token 파싱
      final newAccessToken = refreshResponse.headers['authorization']![0];
      final newRefreshToken =
          refreshResponse.headers['authorization-refresh']![0];

      // 기기에 저장된 AccessToken과 RefreshToken 갱신
      await secureStorage.write(key: 'ACCESS_TOKEN', value: newAccessToken);
      await secureStorage.write(key: 'REFRESH_TOKEN', value: newRefreshToken);

      // 토큰 만료로 수행하지 못했던 API 요청에 담겼던 AccessToken 갱신
      error.requestOptions.headers['Authorization'] = 'Bearer $newAccessToken';

      // 수행하지 못했던 API 요청의 복사본 생성
      final clonedRequest = await dio.request(
        error.requestOptions.path,
        options: Options(
          method: error.requestOptions.method,
          headers: error.requestOptions.headers,
        ),
        data: error.requestOptions.data,
        queryParameters: error.requestOptions.queryParameters,
      );

      // API 복사본으로 재요청
      return handler.resolve(clonedRequest);
    }

    return handler.next(error);
  }));

  return dio;
}
