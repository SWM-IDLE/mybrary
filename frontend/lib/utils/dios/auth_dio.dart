import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/logics/parse_utils.dart';

Future<Dio> authDio(BuildContext context) async {
  var dio = Dio();

  const secureStorage = FlutterSecureStorage();

  dio.interceptors.clear();

  dio.interceptors.add(InterceptorsWrapper(onRequest: (options, handler) async {
    final accessToken = await secureStorage.read(key: accessTokenKey);

    final jwtPayload = parseJwt(accessToken!);
    final int expiredTime = jwtPayload['exp'];

    DateTime expiredDateTime =
        DateTime.fromMillisecondsSinceEpoch(expiredTime * 1000);
    final int expiredTimeStamp = expiredDateTime.millisecondsSinceEpoch;

    DateTime todayDateTime = DateTime.now();
    final int todayTimeStamp = todayDateTime.millisecondsSinceEpoch;

    if (expiredTimeStamp < todayTimeStamp) {
      final accessToken = await secureStorage.read(key: accessTokenKey);
      final refreshToken = await secureStorage.read(key: refreshTokenKey);

      var refreshDio = Dio();

      refreshDio.options.headers[accessTokenHeaderKey] =
          '$jwtHeaderBearer$accessToken';
      refreshDio.options.headers[refreshTokenHeaderKey] =
          '$jwtHeaderBearer$refreshToken';

      final refreshResponse = await refreshDio.get(
        getApi(API.getRefreshToken),
      );

      final newAccessToken = refreshResponse.headers[accessTokenHeaderKey]![0];
      final newRefreshToken =
          refreshResponse.headers[refreshTokenHeaderKey]![0];

      await secureStorage.write(key: accessTokenKey, value: newAccessToken);
      await secureStorage.write(key: refreshTokenKey, value: newRefreshToken);

      options.headers[accessTokenHeaderKey] = '$jwtHeaderBearer$newAccessToken';

      final clonedRequest = await dio.request(
        options.path,
        options: Options(
          method: options.method,
          headers: options.headers,
        ),
        data: options.data,
        queryParameters: options.queryParameters,
      );

      return handler.resolve(clonedRequest);
    }

    options.headers[accessTokenHeaderKey] = '$jwtHeaderBearer$accessToken';
    return handler.next(options);
  }, onResponse: (Response response, ResponseInterceptorHandler handler) {
    return handler.next(response);
  }, onError: (error, handler) async {
    if (error.response?.statusCode == 400) {
      log('ERROR: Refresh 토큰 만료에 대한 서버 에러가 발생했습니다.');
      await secureStorage.deleteAll();

      if (context.mounted) {
        Navigator.of(context).pushNamedAndRemoveUntil(
          '/signin',
          (Route<dynamic> route) => false,
        );
      }
    }
    return handler.reject(error);
  }));

  return dio;
}
