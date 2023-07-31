import 'package:dio/dio.dart';
import 'package:mybrary/data/datasource/profile/profile_datasource.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileRepository {
  final ProfileDataSource profileDataSource = ProfileDataSource();

  Future<ProfileResponseData> getProfileData() {
    return profileDataSource.getProfileData();
  }

  Future<ProfileResponseData> editProfileData({
    required String newNickname,
    required String introduction,
  }) {
    return profileDataSource.editProfileData(
      newNickname,
      introduction,
    );
  }

  Future<ProfileImageResponseData> getProfileImage() {
    return profileDataSource.getProfileImage();
  }

  Future<ProfileImageResponseData> editProfileImage({
    required FormData newProfileImage,
  }) {
    return profileDataSource.editProfileImage(newProfileImage);
  }

  Future<ProfileImageResponseData> deleteProfileImage() {
    return profileDataSource.deleteProfileImage();
  }
}
