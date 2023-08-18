import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/profile/interests_datasource.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';

class InterestsRepository {
  final InterestsDataSource _interestsDataSource = InterestsDataSource();

  Future<InterestCategoriesResponseData> getInterestCategories({
    required BuildContext context,
  }) {
    return _interestsDataSource.getInterestCategories(context);
  }

  Future<MyInterestsResponseData> getMyInterestsCategories({
    required BuildContext context,
    required String userId,
  }) {
    return _interestsDataSource.getMyInterestsCategories(context, userId);
  }

  Future<MyInterestsResponseData> editMyInterests({
    required BuildContext context,
    required String userId,
    required List<CategoriesResponses> categoriesResponses,
  }) {
    return _interestsDataSource.updateMyInterests(
      context,
      userId,
      categoriesResponses,
    );
  }
}
