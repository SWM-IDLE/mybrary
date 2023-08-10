import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class BookDataSource {
  Future<List<BookListResponseData>> getInterestBooks(
      String userId, String? order) async {
    Dio dio = DioService().to();
    final getInterestBooksResponse = await dio.get(
      '${getBookServiceApi(API.getInterestBooks)}/$userId/interest?order=$order',
      options: Options(headers: {'User-Id': userId}),
    );

    log('관심 도서 조회 응답값: $getInterestBooksResponse');
    final BookListResponse result = commonResponseResult(
      getInterestBooksResponse,
      () => BookListResponse(
        status: getInterestBooksResponse.data['status'],
        message: getInterestBooksResponse.data['message'],
        data: List<BookListResponseData>.from(
          getInterestBooksResponse.data['data'].map(
            (x) => BookListResponseData.fromJson(x),
          ),
        ),
      ),
    );

    return result.data!;
  }

  Future<InterestBookResponseData> createOrDeleteInterestBook(
      String userId, String isbn13) async {
    Dio dio = DioService().to();
    final createOrDeleteInterestBookResponse = await dio.post(
      '${getBookServiceApi(API.createOrDeleteInterestBook)}/$isbn13/interest',
      options: Options(headers: {'User-Id': userId}),
    );

    log('관심 도서 등록 및 삭제 응답값: $createOrDeleteInterestBookResponse');
    final InterestBookResponse result = commonResponseResult(
      createOrDeleteInterestBookResponse,
      () => InterestBookResponse(
        status: createOrDeleteInterestBookResponse.data['status'],
        message: createOrDeleteInterestBookResponse.data['message'],
        data: InterestBookResponseData.fromJson(
          createOrDeleteInterestBookResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }

  Future<List<MyBooksResponseData>> getMyBooks(String userId) async {
    Dio dio = DioService().to();
    final getMyBooksResponse = await dio.get(
      '${getBookServiceApi(API.getMyBooks)}/$userId/mybooks',
      options: Options(headers: {'User-Id': userId}),
    );

    log('마이북 전체 조회 응답값: $getMyBooksResponse');
    final MyBooksResponse result = commonResponseResult(
      getMyBooksResponse,
      () => MyBooksResponse(
        status: getMyBooksResponse.data['status'],
        message: getMyBooksResponse.data['message'],
        data: List<MyBooksResponseData>.from(
          getMyBooksResponse.data['data'].map(
            (x) => MyBooksResponseData.fromJson(x),
          ),
        ),
      ),
    );

    return result.data!;
  }

  Future<MyBookDetailResponseData> getMyBookDetail(
      String userId, int myBookId) async {
    Dio dio = DioService().to();
    final getMyBookDetailResponse = await dio.get(
      '${getBookServiceApi(API.getMyBookDetail)}/$myBookId',
      options: Options(headers: {'User-Id': userId}),
    );

    log('마이북 상세 조회 응답값: $getMyBookDetailResponse');
    final MyBookDetailResponse result = commonResponseResult(
      getMyBookDetailResponse,
      () => MyBookDetailResponse(
        status: getMyBookDetailResponse.data['status'],
        message: getMyBookDetailResponse.data['message'],
        data: MyBookDetailResponseData.fromJson(
          getMyBookDetailResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }
}
