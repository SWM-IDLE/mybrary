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
    );
    _dio = Dio(options);
  }

  Dio to() {
    return _dio;
  }
}
