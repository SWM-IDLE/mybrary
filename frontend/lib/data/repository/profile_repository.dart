import 'package:dio/dio.dart';
import 'package:mybrary/data/datasource/profile/profile_datasource.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileRepository {
  final ProfileDataSource profileDataSource = ProfileDataSource();

  Future<ProfileResponseData> getProfileData() {
    return profileDataSource.getProfileData();
  }

  Future<ProfileResponseData> updateProfileData({
    required String newNickname,
    required String introduction,
  }) {
    return profileDataSource.updateProfileData(
      newNickname,
      introduction,
    );
  }

  Future<ProfileImageResponseData> getProfileImage() {
    return profileDataSource.getProfileImage();
  }

  Future<ProfileImageResponseData> updateProfileImage({
    required FormData newProfileImage,
  }) {
    return profileDataSource.updateProfileImage(newProfileImage);
  }

  Future<ProfileImageResponseData> deleteProfileImage() {
    return profileDataSource.deleteProfileImage();
  }
}
