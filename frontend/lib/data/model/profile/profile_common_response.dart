import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileCommonData {
  ProfileResponseData profileData;
  ProfileImageResponseData profileImageData;
  MyInterestsResponseData myInterestsData;
  FollowerResponseData followerData;
  FollowingResponseData followingData;

  ProfileCommonData({
    required this.profileData,
    required this.profileImageData,
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

class UserProfileCommonData {
  ProfileResponseData profileData;
  ProfileImageResponseData profileImageData;
  MyInterestsResponseData myInterestsData;
  FollowerResponseData followerData;
  FollowingResponseData followingData;
  List<MyBooksResponseData> myBooksData;

  UserProfileCommonData({
    required this.profileData,
    required this.profileImageData,
    required this.myInterestsData,
    required this.followerData,
    required this.followingData,
    required this.myBooksData,
  });
}
