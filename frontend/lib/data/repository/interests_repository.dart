import 'package:mybrary/data/datasource/profile/interests_datasource.dart';
import 'package:mybrary/data/model/profile/interest_categories_response.dart';

class InterestsRepository {
  final InterestsDataSource _interestsDataSource = InterestsDataSource();

  Future<InterestCategoriesResponseData> getInterestCategories() {
    return _interestsDataSource.getInterestCategories();
  }
}
