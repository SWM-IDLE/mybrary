import 'package:dio/dio.dart';
import 'package:mybrary/data/datasource/profile/profile_datasource.dart';
import 'package:mybrary/data/model/common/common_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileRepository {
  final ProfileDataSource profileDataSource = ProfileDataSource();

  Future<ProfileResponseData> getProfileData({
    required String userId,
  }) {
    return profileDataSource.getProfileData(userId);
  }

  Future<ProfileResponseData> updateProfileData({
    required String userId,
    required String newNickname,
    required String introduction,
  }) {
    return profileDataSource.updateProfileData(
      userId,
      newNickname,
      introduction,
    );
  }

  Future<ProfileImageResponseData> getProfileImage({
    required String userId,
  }) {
    return profileDataSource.getProfileImage(userId);
  }

  Future<ProfileImageResponseData> updateProfileImage({
    required String userId,
    required FormData newProfileImage,
  }) {
    return profileDataSource.updateProfileImage(
      userId,
      newProfileImage,
    );
  }

  Future<ProfileImageResponseData> deleteProfileImage({
    required String userId,
  }) {
    return profileDataSource.deleteProfileImage(userId);
  }

  Future<CommonResponse> deleteAccount({
    required String userId,
  }) {
    return profileDataSource.deleteAccount(userId);
  }
}
