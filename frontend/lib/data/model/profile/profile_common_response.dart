import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileCommonData {
  ProfileResponseData profileData;
  MyInterestsResponseData myInterestsData;
  FollowerResponseData followerData;
  FollowingResponseData followingData;

  ProfileCommonData({
    required this.profileData,
    required this.myInterestsData,
    required this.followerData,
    required this.followingData,
  });
}

class FollowCommonData {
  FollowerResponseData followerData;
  FollowingResponseData followingData;

  FollowCommonData({
    required this.followerData,
    required this.followingData,
  });
}
