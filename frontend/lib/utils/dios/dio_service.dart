import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/network/api.dart';

class DioService {
  static final DioService _dioService = DioService._internal();
  factory DioService() => _dioService;

  static Dio _dio = Dio();

  DioService._internal() {
    BaseOptions options = BaseOptions(
      baseUrl: baseUrl,
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
      sendTimeout: const Duration(seconds: 10),
      // headers: {},
    );
    _dio = Dio(options);
    _dio.interceptors.add(DioInterceptor());
  }

  Dio to() {
    return _dio;
  }
}

class DioInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    log("API 요청 경로: ${options.path}");
    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    log("${response.requestOptions.path} 의 응답 코드: ${response.statusCode}");
    super.onResponse(response, handler);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    log("ERROR: Server 에러 <${err.error}>");
    log("ERROR: Server 에러 메세지 <${err.message}>");
    super.onError(err, handler);
  }
}
