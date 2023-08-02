import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/network/api.dart';

class InterestsDataSource {
  Future<InterestCategoriesResponseData> getInterestCategories() async {
    final dio = Dio();
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
    String userId,
  ) async {
    final dio = Dio();
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

    return result.data;
  }

  Future<MyInterestsResponseData> editMyInterests(
    String userId,
    List<CategoriesResponses> categoriesResponses,
  ) async {
    final dio = Dio();
    final myInterestsEditResponse = await dio.put(
      '${getApi(API.getUserInterests)}/$userId/interests',
      options: Options(headers: {'User-Id': 'testId'}),
      data: {'interestRequests': categoriesResponses},
    );

    log('프로필 수정 응답값: $myInterestsEditResponse');
    final MyInterestsResponse result = commonResponseResult(
      myInterestsEditResponse,
      () => MyInterestsResponse(
        status: myInterestsEditResponse.data['status'],
        message: myInterestsEditResponse.data['message'],
        data: MyInterestsResponseData.fromJson(
          myInterestsEditResponse.data['data'],
        ),
      ),
    );

    return result.data;
  }
}
