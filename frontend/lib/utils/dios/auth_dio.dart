import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/dio_service.dart';
import 'package:mybrary/utils/logics/parse_utils.dart';

Future<Dio> authDio(BuildContext context) async {
  Dio dio = DioService().to();

  const secureStorage = FlutterSecureStorage();

  dio.interceptors.clear();

  dio.interceptors.add(InterceptorsWrapper(onRequest: (options, handler) async {
    final accessToken = await secureStorage.read(key: accessTokenKey);

    final jwtPayload = parseJwt(accessToken!);

    final int todayTimeStamp = DateTime.now().millisecondsSinceEpoch ~/ 1000;
    final int expiredTimeStamp = jwtPayload['exp'];

    if (expiredTimeStamp < todayTimeStamp) {
      options.data = {expiredKey: true};
      return handler.reject(
        DioException(requestOptions: options),
        true,
      );
    }

    options.headers[accessTokenHeaderKey] = '$jwtHeaderBearer$accessToken';
    return handler.next(options);
  }, onError: (error, handler) async {
    if (error.response?.statusCode == 401) {
      log('ERROR: Access 토큰 만료에 대한 클라이언트 및 서버 에러가 발생했습니다.');

      final accessToken = await secureStorage.read(key: accessTokenKey);
      final refreshToken = await secureStorage.read(key: refreshTokenKey);

      Dio refreshDio = DioService().to();

      refreshDio.interceptors.clear();
      refreshDio.interceptors
          .add(InterceptorsWrapper(onError: (error, handler) async {
        if (error.response?.statusCode == 401) {
          log('ERROR: Refresh 토큰 만료에 대한 서버 에러가 발생했습니다.');
          await secureStorage.deleteAll();

          if (context.mounted) {
            Navigator.of(context).pushNamedAndRemoveUntil(
              '/signin',
              (Route<dynamic> route) => false,
            );
          }
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

    return handler.next(error);
  }));

  return dio;
}
