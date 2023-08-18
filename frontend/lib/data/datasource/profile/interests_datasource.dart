import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class InterestsDataSource {
  Future<InterestCategoriesResponseData> getInterestCategories(
      BuildContext context) async {
    final dio = await authDio(context);
    final interestCategoriesResponse = await dio.get(
      getApi(API.getInterestCategories),
    );

    log('마이 관심사 전체 응답값: $interestCategoriesResponse');
    final InterestCategoriesResponse result = commonResponseResult(
      interestCategoriesResponse,
      () => InterestCategoriesResponse(
        status: interestCategoriesResponse.data['status'],
        message: interestCategoriesResponse.data['message'],
        data: InterestCategoriesResponseData.fromJson(
          interestCategoriesResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }

  Future<MyInterestsResponseData> getMyInterestsCategories(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final myInterestsResponse = await dio.get(
      '${getApi(API.getUserInterests)}/$userId/interests',
    );

    log('마이 관심사 사용자 응답값: $myInterestsResponse');
    final MyInterestsResponse result = commonResponseResult(
      myInterestsResponse,
      () => MyInterestsResponse(
        status: myInterestsResponse.data['status'],
        message: myInterestsResponse.data['message'],
        data: MyInterestsResponseData.fromJson(
          myInterestsResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<MyInterestsResponseData> updateMyInterests(
    BuildContext context,
    String userId,
    List<CategoriesResponses> categoriesResponses,
  ) async {
    final dio = await authDio(context);
    final myInterestsUpdateResponse = await dio.put(
      '${getApi(API.updateUserInterests)}/$userId/interests',
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
      data: {'interestRequests': categoriesResponses},
    );

    log('마이 관심사 수정 응답값: $myInterestsUpdateResponse');
    final MyInterestsResponse result = commonResponseResult(
      myInterestsUpdateResponse,
      () => MyInterestsResponse(
        status: myInterestsUpdateResponse.data['status'],
        message: myInterestsUpdateResponse.data['message'],
        data: MyInterestsResponseData.fromJson(
          myInterestsUpdateResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }
}
