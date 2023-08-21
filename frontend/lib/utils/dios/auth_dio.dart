import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

Future<Dio> authDio(BuildContext context) async {
  var dio = DioService().to();

  const secureStorage = FlutterSecureStorage();

  dio.interceptors.clear();
  dio.interceptors
      .add(QueuedInterceptorsWrapper(onRequest: (options, handler) async {
    final accessToken = await secureStorage.read(key: accessTokenKey);
    log("API 요청 경로: ${options.path}");

    options.headers[accessTokenHeaderKey] = '$jwtHeaderBearer$accessToken';
    return handler.next(options);
  }, onError: (error, handler) async {
    log("ERROR: Server 에러 코드 <${error.response!.statusCode}>");
    log("ERROR: Server 에러 메세지 <${error.response!.data.toString()}>");

    if (error.response?.statusCode == 401) {
      log('ERROR: Access 토큰 만료에 대한 서버 에러가 발생했습니다.');

      final accessToken = await secureStorage.read(key: accessTokenKey);
      final refreshToken = await secureStorage.read(key: refreshTokenKey);

      var refreshDio = Dio();

      refreshDio.interceptors.clear();
      refreshDio.interceptors
          .add(InterceptorsWrapper(onError: (error, handler) async {
        if (error.response?.statusCode == 401) {
          log('ERROR: Refresh 토큰 만료에 대한 서버 에러가 발생했습니다.');
          await secureStorage.deleteAll();

          if (!context.mounted) return;
          Navigator.of(context).pushNamedAndRemoveUntil(
              '/signin', (Route<dynamic> route) => false);
        }
        return handler.next(error);
      }));

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

      error.requestOptions.headers[accessTokenHeaderKey] =
          '$jwtHeaderBearer$newAccessToken';

      final clonedRequest = await dio.request(
        error.requestOptions.path,
        options: Options(
          method: error.requestOptions.method,
          headers: error.requestOptions.headers,
        ),
        data: error.requestOptions.data,
        queryParameters: error.requestOptions.queryParameters,
      );

      return handler.resolve(clonedRequest);
    }
  }));

  return dio;
}
