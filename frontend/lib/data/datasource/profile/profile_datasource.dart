import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/dio_service.dart';

class ProfileDataSource {
  Future<ProfileResponseData> getProfileData() async {
    Dio dio = DioService().to();
    final profileResponse = await dio.get(getApi(API.getUserProfile),
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

  Future<ProfileResponseData> editProfileData(
    String newNickname,
    String introduction,
  ) async {
    Dio dio = DioService().to();
    final profileEditResponse = await dio.put(
      getApi(API.editUserProfile),
      options: Options(headers: {'User-Id': 'testId'}),
      data: {'nickname': newNickname, 'introduction': introduction},
    );

    log('프로필 수정 응답값: $profileEditResponse');
    final ProfileResponse result = commonResponseResult(
      profileEditResponse,
      () => ProfileResponse(
        status: profileEditResponse.data['status'],
        message: profileEditResponse.data['message'],
        data: ProfileResponseData.fromJson(
          profileEditResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> getProfileImage() async {
    Dio dio = DioService().to();
    final profileImageResponse = await dio.get(getApi(API.getUserProfileImage),
        options: Options(headers: {'User-Id': 'testId'}));

    log('프로필 이미지 조회 응답값: $profileImageResponse');
    final ProfileImageResponse result = commonResponseResult(
      profileImageResponse,
      () => ProfileResponse(
        status: profileImageResponse.data['status'],
        message: profileImageResponse.data['message'],
        data: ProfileResponseData.fromJson(
          profileImageResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> editProfileImage(
      FormData newProfileImage) async {
    Dio dio = DioService().to();
    final profileImageEditResponse = await dio.put(
      getApi(API.editUserProfileImage),
      options: Options(
        headers: {'User-Id': 'testId'},
        contentType: 'multipart/form-data',
      ),
      data: newProfileImage,
    );

    log('프로필 이미지 수정 응답값: $profileImageEditResponse');
    final ProfileImageResponse result = commonResponseResult(
      profileImageEditResponse,
      () => ProfileImageResponse(
        status: profileImageEditResponse.data['status'],
        message: profileImageEditResponse.data['message'],
        data: ProfileImageResponseData.fromJson(
          profileImageEditResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<ProfileImageResponseData> deleteProfileImage() async {
    Dio dio = DioService().to();
    final profileImageDeleteResponse = await dio.delete(
      getApi(API.editUserProfileImage),
      options: Options(
        headers: {'User-Id': 'testId'},
      ),
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
}
