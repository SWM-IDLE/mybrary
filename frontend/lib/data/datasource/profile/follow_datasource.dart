import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/network/api.dart';

class FollowDataSource {
  Future<FollowerResponseData> getFollower(String userId) async {
    final dio = Dio();
    final userFollowerResponse = await dio.get(
      getApi(API.getUserFollowers),
      options: Options(
        headers: {'User-Id': userId},
      ),
    );

    log('유저 팔로워 응답값: $userFollowerResponse');
    final FollowerResponse result = commonResponseResult(
      userFollowerResponse,
      () => FollowerResponse(
        status: userFollowerResponse.data['status'],
        message: userFollowerResponse.data['message'],
        data: FollowerResponseData.fromJson(
          userFollowerResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<FollowingResponseData> getFollowings(String userId) async {
    final dio = Dio();
    final userFollowingResponse = await dio.get(
      getApi(API.getUserFollowings),
      options: Options(
        headers: {'User-Id': userId},
      ),
    );

    log('유저 팔로잉 응답값: $userFollowingResponse');
    final FollowingResponse result = commonResponseResult(
      userFollowingResponse,
      () => FollowingResponse(
        status: userFollowingResponse.data['status'],
        message: userFollowingResponse.data['message'],
        data: FollowingResponseData.fromJson(
          userFollowingResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }

  Future<FollowingResponseData?> updateFollowing(
    String userId,
    String targetId,
  ) async {
    final dio = Dio();
    final updateFollowingResponse = await dio.post(
      getApi(API.updateUserFollowing),
      options: Options(
        headers: {'User-Id': userId},
      ),
      data: {'targetId': targetId},
    );

    log('사용자 팔로우 응답값: $updateFollowingResponse');
    final FollowingResponse result = commonResponseResult(
      updateFollowingResponse,
      () => FollowingResponse(
        status: updateFollowingResponse.data['status'],
        message: updateFollowingResponse.data['message'],
        data: updateFollowingResponse.data['data'],
      ),
    );

    return result.data;
  }

  Future<FollowerResponseData?> deleteFollower(
    String userId,
    String sourceId,
  ) async {
    final dio = Dio();
    final deleteFollowerResponse = await dio.delete(
      getApi(API.deleteUserFollower),
      options: Options(
        headers: {'User-Id': userId},
      ),
      data: {'sourceId': sourceId},
    );

    log('팔로워 삭제 응답값: $deleteFollowerResponse');
    final FollowerResponse result = commonResponseResult(
      deleteFollowerResponse,
      () => FollowerResponse(
        status: deleteFollowerResponse.data['status'],
        message: deleteFollowerResponse.data['message'],
        data: deleteFollowerResponse.data['data'],
      ),
    );

    return result.data;
  }

  Future<FollowingResponseData?> deleteFollowing(
    String userId,
    String targetId,
  ) async {
    final dio = Dio();
    final deleteFollowingResponse = await dio.delete(
      getApi(API.deleteUserFollowing),
      options: Options(
        headers: {'User-Id': userId},
      ),
      data: {'targetId': targetId},
    );

    log('팔로잉 삭제 응답값: $deleteFollowingResponse');
    final FollowingResponse result = commonResponseResult(
      deleteFollowingResponse,
      () => FollowingResponse(
        status: deleteFollowingResponse.data['status'],
        message: deleteFollowingResponse.data['message'],
        data: deleteFollowingResponse.data['data'],
      ),
    );

    return result.data;
  }
}
