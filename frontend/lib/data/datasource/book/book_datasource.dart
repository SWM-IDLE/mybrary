import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_record_reponse.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/common/common_response.dart';
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

  Future<List<MyBooksResponseData>> getMyBooks(
    String userId,
    String order,
    String readStatus,
  ) async {
    Dio dio = DioService().to();
    final getMyBooksResponse = await dio.get(
      '${getBookServiceApi(API.getMyBooks)}/$userId/mybooks?order=$order&readStatus=$readStatus',
      options: Options(headers: {'User-Id': userId}),
    );

    log('마이북 조건별 조회 응답값: $getMyBooksResponse');
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

  Future<CommonResponse> createMyBook(String userId, String isbn13) async {
    Dio dio = DioService().to();
    final createMyBookResponse = await dio.post(
      getBookServiceApi(API.createMyBook),
      options: Options(headers: {'User-Id': userId}),
      data: {'isbn13': isbn13},
    );

    log('마이북 등록 응답값: $createMyBookResponse');
    final CommonResponse result = commonResponseResult(
      createMyBookResponse,
      () => CommonResponse(
        status: createMyBookResponse.data['status'],
        message: createMyBookResponse.data['message'],
        data: null,
      ),
    );

    return result;
  }

  Future<CommonResponse> deleteMyBook(String userId, int myBookId) async {
    Dio dio = DioService().to();
    final deleteMyBookResponse = await dio.delete(
      '${getBookServiceApi(API.deleteMyBook)}/$myBookId',
      options: Options(headers: {'User-Id': userId}),
    );

    log('마이북 삭제 응답값: $deleteMyBookResponse');
    final CommonResponse result = commonResponseResult(
      deleteMyBookResponse,
      () => CommonResponse(
        status: deleteMyBookResponse.data['status'],
        message: deleteMyBookResponse.data['message'],
        data: null,
      ),
    );

    return result;
  }

  Future<MyBookRecordResponseData> updateMyBookRecord(String userId,
      int myBookId, MyBookRecordResponseData myBookRecordData) async {
    Dio dio = DioService().to();
    final updateMyBookRecordResponse = await dio.put(
      '${getBookServiceApi(API.updateMyBookRecord)}/$myBookId',
      options: Options(headers: {'User-Id': userId}),
      data: myBookRecordData.toJson(),
    );

    log('마이북 기록 수정 응답값: $updateMyBookRecordResponse');
    final MyBookRecordResponse result = commonResponseResult(
      updateMyBookRecordResponse,
      () => MyBookRecordResponse(
        status: updateMyBookRecordResponse.data['status'],
        message: updateMyBookRecordResponse.data['message'],
        data: updateMyBookRecordResponse.data['data'] != null
            ? MyBookRecordResponseData.fromJson(
                updateMyBookRecordResponse.data['data'])
            : null,
      ),
    );

    return result.data!;
  }

  Future<MyBookReviewResponseData?> getMyBookReview(int myBookId) async {
    Dio dio = DioService().to();
    final getMyBookReviewResponse = await dio.get(
      '${getBookServiceApi(API.getMyBookReview)}/$myBookId/review',
    );

    log('마이북 리뷰 조회 응답값: $getMyBookReviewResponse');
    final MyBookReviewResponse result = commonResponseResult(
      getMyBookReviewResponse,
      () => MyBookReviewResponse(
        status: getMyBookReviewResponse.data['status'],
        message: getMyBookReviewResponse.data['message'],
        data: getMyBookReviewResponse.data['data'] != null
            ? MyBookReviewResponseData.fromJson(
                getMyBookReviewResponse.data['data'])
            : null,
      ),
    );

    return result.data;
  }

  Future<CommonResponse> createMyBookReview(
    String userId,
    int myBookId,
    String content,
    double starRating,
  ) async {
    Dio dio = DioService().to();
    final createMyBookReviewResponse = await dio.post(
      '${getBookServiceApi(API.createMyBookReview)}/$myBookId/reviews',
      options: Options(headers: {'User-Id': userId}),
      data: {'content': content, 'starRating': '$starRating'},
    );

    log('마이북 리뷰 작성 응답값: $createMyBookReviewResponse');
    final CommonResponse result = commonResponseResult(
      createMyBookReviewResponse,
      () => CommonResponse(
        status: createMyBookReviewResponse.data['status'],
        message: createMyBookReviewResponse.data['message'],
        data: null,
      ),
    );

    return result;
  }

  Future<MyBookReviewUpdateResponseData> updateMyBookReview(
    String userId,
    int reviewId,
    String content,
    double starRating,
  ) async {
    Dio dio = DioService().to();
    final updateMyBookReviewResponse = await dio.put(
      '${getBookServiceApi(API.updateMyBookReview)}/$reviewId',
      options: Options(headers: {'User-Id': userId}),
      data: {'content': content, 'starRating': '$starRating'},
    );

    log('마이북 리뷰 수정 응답값: $updateMyBookReviewResponse');
    final MyBookReviewUpdateResponse result = commonResponseResult(
      updateMyBookReviewResponse,
      () => MyBookReviewUpdateResponse(
        status: updateMyBookReviewResponse.data['status'],
        message: updateMyBookReviewResponse.data['message'],
        data: updateMyBookReviewResponse.data['data'] != null
            ? MyBookReviewUpdateResponseData.fromJson(
                updateMyBookReviewResponse.data['data'])
            : null,
      ),
    );

    return result.data!;
  }
}
