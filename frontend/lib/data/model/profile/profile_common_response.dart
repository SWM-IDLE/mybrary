import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileCommonData {
  ProfileResponseData profileData;
  MyInterestsResponseData myInterestsData;

  ProfileCommonData({
    required this.profileData,
    required this.myInterestsData,
  });
}
