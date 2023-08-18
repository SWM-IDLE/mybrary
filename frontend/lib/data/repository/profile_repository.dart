import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/profile/profile_datasource.dart';
import 'package:mybrary/data/model/common/common_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';

class ProfileRepository {
  final ProfileDataSource profileDataSource = ProfileDataSource();

  Future<ProfileResponseData> getProfileData({
    required BuildContext context,
    required String userId,
  }) {
    return profileDataSource.getProfileData(context, userId);
  }

  Future<ProfileResponseData> updateProfileData({
    required BuildContext context,
    required String userId,
    required String newNickname,
    required String introduction,
  }) {
    return profileDataSource.updateProfileData(
      context,
      userId,
      newNickname,
      introduction,
    );
  }

  Future<ProfileImageResponseData> getProfileImage({
    required BuildContext context,
    required String userId,
  }) {
    return profileDataSource.getProfileImage(context, userId);
  }

  Future<ProfileImageResponseData> updateProfileImage({
    required BuildContext context,
    required String userId,
    required FormData newProfileImage,
  }) {
    return profileDataSource.updateProfileImage(
      context,
      userId,
      newProfileImage,
    );
  }

  Future<ProfileImageResponseData> deleteProfileImage({
    required BuildContext context,
    required String userId,
  }) {
    return profileDataSource.deleteProfileImage(context, userId);
  }

  Future<CommonResponse> deleteAccount({
    required BuildContext context,
    required String userId,
  }) {
    return profileDataSource.deleteAccount(context, userId);
  }
}
