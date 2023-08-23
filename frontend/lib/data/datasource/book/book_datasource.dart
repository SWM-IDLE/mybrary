import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/interest_book_response.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_record_reponse.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/common/common_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class BookDataSource {
  Future<List<BookListResponseData>> getInterestBooks(
    BuildContext context,
    String userId,
    String? order,
  ) async {
    final dio = await authDio(context);
    final getInterestBooksResponse = await dio.get(
      '${getApi(API.getInterestBooks)}/$userId/interest?order=$order',
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
    BuildContext context,
    String userId,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final createOrDeleteInterestBookResponse = await dio.post(
      '${getApi(API.createOrDeleteInterestBook)}/$isbn13/interest',
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
    BuildContext context,
    String userId,
    String order,
    String readStatus,
  ) async {
    final dio = await authDio(context);
    final getMyBooksResponse = await dio.get(
      '${getApi(API.getMyBooks)}/$userId/mybooks?order=$order&readStatus=$readStatus',
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
    BuildContext context,
    String userId,
    int myBookId,
  ) async {
    final dio = await authDio(context);
    final getMyBookDetailResponse = await dio.get(
      '${getApi(API.getMyBookDetail)}/$myBookId',
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

  Future<CommonResponse> createMyBook(
    BuildContext context,
    String userId,
    String isbn13,
  ) async {
    final dio = await authDio(context);
    final createMyBookResponse = await dio.post(
      getApi(API.createMyBook),
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
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

  Future<CommonResponse> deleteMyBook(
    BuildContext context,
    String userId,
    int myBookId,
  ) async {
    final dio = await authDio(context);
    final deleteMyBookResponse = await dio.delete(
      '${getApi(API.deleteMyBook)}/$myBookId',
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

  Future<MyBookRecordResponseData> updateMyBookRecord(
    BuildContext context,
    String userId,
    int myBookId,
    MyBookRecordResponseData myBookRecordData,
  ) async {
    final dio = await authDio(context);
    final updateMyBookRecordResponse = await dio.put(
      '${getApi(API.updateMyBookRecord)}/$myBookId',
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
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

  Future<MyBookReviewResponseData?> getMyBookReview(
    BuildContext context,
    int myBookId,
  ) async {
    final dio = await authDio(context);
    final getMyBookReviewResponse = await dio.get(
      '${getApi(API.getMyBookReview)}/$myBookId/review',
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
    BuildContext context,
    String userId,
    int myBookId,
    String content,
    double starRating,
  ) async {
    final dio = await authDio(context);
    final createMyBookReviewResponse = await dio.post(
      '${getApi(API.createMyBookReview)}/$myBookId/reviews',
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
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
    BuildContext context,
    String userId,
    int reviewId,
    String content,
    double starRating,
  ) async {
    final dio = await authDio(context);
    final updateMyBookReviewResponse = await dio.put(
      '${getApi(API.updateMyBookReview)}/$reviewId',
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
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

  Future<CommonResponse> deleteMyBookReview(
    BuildContext context,
    String userId,
    int reviewId,
  ) async {
    final dio = await authDio(context);
    final deleteMyBookReviewResponse = await dio.delete(
      '${getApi(API.deleteMyBookReview)}/$reviewId',
      options: Options(headers: {'User-Id': userId}),
    );

    log('마이북 리뷰 삭제 응답값: $deleteMyBookReviewResponse');
    final CommonResponse result = commonResponseResult(
      deleteMyBookReviewResponse,
      () => CommonResponse(
        status: deleteMyBookReviewResponse.data['status'],
        message: deleteMyBookReviewResponse.data['message'],
        data: null,
      ),
    );

    return result;
  }
}
