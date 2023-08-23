import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/common/common_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class ProfileDataSource {
  Future<ProfileResponseData> getProfileData(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final profileResponse = await dio.get(
        '${getApi(API.getUserProfile)}/$userId/profile',
        options: Options(headers: {'User-Id': 'testId'}));

    log('프로필 조회 응답값: $profileResponse');
    final ProfileResponse result = commonResponseResult(
      profileResponse,
      () => ProfileResponse(
        status: profileResponse.data['status'],
        message: profileResponse.data['message'],
        data: ProfileResponseData.fromJson(
          profileResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileResponseData> updateProfileData(
    BuildContext context,
    String userId,
    String newNickname,
    String introduction,
  ) async {
    final dio = await authDio(context);
    final profileUpdateResponse = await dio.put(
      '${getApi(API.updateUserProfile)}/$userId/profile',
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
      ),
      data: {'nickname': newNickname, 'introduction': introduction},
    );

    log('프로필 수정 응답값: $profileUpdateResponse');
    final ProfileResponse result = commonResponseResult(
      profileUpdateResponse,
      () => ProfileResponse(
        status: profileUpdateResponse.data['status'],
        message: profileUpdateResponse.data['message'],
        data: ProfileResponseData.fromJson(
          profileUpdateResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> getProfileImage(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final profileImageResponse = await dio.get(
        '${getApi(API.getUserProfileImage)}/$userId/profile/image?size=small',
        options: Options(headers: {'User-Id': 'testId'}));

    log('프로필 이미지 조회 응답값: $profileImageResponse');
    final ProfileImageResponse result = commonResponseResult(
      profileImageResponse,
      () => ProfileImageResponse(
        status: profileImageResponse.data['status'],
        message: profileImageResponse.data['message'],
        data: ProfileImageResponseData.fromJson(
          profileImageResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> updateProfileImage(
    BuildContext context,
    String userId,
    FormData newProfileImage,
  ) async {
    final dio = await authDio(context);
    final profileImageUpdateResponse = await dio.put(
      '${getApi(API.updateUserProfileImage)}/$userId/profile/image',
      options: Options(
        headers: {'User-Id': 'testId'},
        contentType: 'multipart/form-data',
      ),
      data: newProfileImage,
    );

    log('프로필 이미지 수정 응답값: $profileImageUpdateResponse');
    final ProfileImageResponse result = commonResponseResult(
      profileImageUpdateResponse,
      () => ProfileImageResponse(
        status: profileImageUpdateResponse.data['status'],
        message: profileImageUpdateResponse.data['message'],
        data: ProfileImageResponseData.fromJson(
          profileImageUpdateResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> deleteProfileImage(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final profileImageDeleteResponse = await dio.delete(
      '${getApi(API.updateUserProfileImage)}/$userId/profile/image',
      options: Options(headers: {'User-Id': 'testId'}),
    );

    log('프로필 이미지 삭제 응답값: $profileImageDeleteResponse');
    final ProfileImageResponse result = commonResponseResult(
      profileImageDeleteResponse,
      () => ProfileImageResponse(
        status: profileImageDeleteResponse.data['status'],
        message: profileImageDeleteResponse.data['message'],
        data: ProfileImageResponseData.fromJson(
          profileImageDeleteResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<CommonResponse> deleteAccount(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final deleteAccountResponse = await dio.delete(
      getApi(API.deleteUserAccount),
      options: Options(headers: {'User-Id': 'testId'}),
    );

    log('계정 삭제 응답값: $deleteAccountResponse');
    final CommonResponse result = commonResponseResult(
      deleteAccountResponse,
      () => CommonResponse(
        status: deleteAccountResponse.data['status'],
        message: deleteAccountResponse.data['message'],
        data: null,
      ),
    );

    return result;
  }
}
