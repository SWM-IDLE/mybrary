import 'package:mybrary/data/datasource/profile/interests_datasource.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';

class InterestsRepository {
  final InterestsDataSource _interestsDataSource = InterestsDataSource();

  Future<InterestCategoriesResponseData> getInterestCategories() {
    return _interestsDataSource.getInterestCategories();
  }

  Future<MyInterestsResponseData> getMyInterestsCategories({
    required String userId,
  }) {
    return _interestsDataSource.getMyInterestsCategories(userId);
  }

  Future<MyInterestsResponseData> editMyInterests({
    required String userId,
    required List<CategoriesResponses> categoriesResponses,
  }) {
    return _interestsDataSource.updateMyInterests(userId, categoriesResponses);
  }
}
