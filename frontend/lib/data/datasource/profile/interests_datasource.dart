import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/network/api.dart';

class InterestsDataSource {
  Future<InterestCategoriesResponseData> getInterestCategories() async {
    final dio = Dio();
    final interestCategoriesResponse = await dio.get(
      getApi(
        API.getInterestCategories,
      ),
    );

    log('마이 관심사 응답값: $interestCategoriesResponse');
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
}
