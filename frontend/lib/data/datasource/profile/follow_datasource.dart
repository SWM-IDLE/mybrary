import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/follow_status_response.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class FollowDataSource {
  Future<FollowerResponseData> getFollower(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final userFollowerResponse = await dio.get(
      '${getApi(API.getUserFollowers)}/$userId/followers',
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

  Future<FollowingResponseData> getFollowings(
    BuildContext context,
    String userId,
  ) async {
    final dio = await authDio(context);
    final userFollowingResponse = await dio.get(
      '${getApi(API.getUserFollowings)}/$userId/followings',
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
    BuildContext context,
    String userId,
    String targetId,
  ) async {
    final dio = await authDio(context);
    final updateFollowingResponse = await dio.post(
      getApi(API.updateUserFollowing),
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
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
    BuildContext context,
    String userId,
    String sourceId,
  ) async {
    final dio = await authDio(context);
    final deleteFollowerResponse = await dio.delete(
      getApi(API.deleteUserFollower),
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
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
    BuildContext context,
    String userId,
    String targetId,
  ) async {
    final dio = await authDio(context);
    final deleteFollowingResponse = await dio.delete(
      getApi(API.deleteUserFollowing),
      options: Options(
        headers: {'User-Id': userId, "Content-Type": headerJsonValue},
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

  Future<FollowStatusResponseData> getUserFollowStatus(
    BuildContext context,
    String userId,
    String targetId,
  ) async {
    final dio = await authDio(context);
    final userFollowStatusResponse = await dio.get(
      '${getApi(API.getUserFollowStatus)}?targetId=$targetId',
      options: Options(
        headers: {'User-Id': userId},
      ),
    );

    log('팔로우 상태 응답값: $userFollowStatusResponse');
    final FollowStatusResponse result = commonResponseResult(
      userFollowStatusResponse,
      () => FollowStatusResponse(
        status: userFollowStatusResponse.data['status'],
        message: userFollowStatusResponse.data['message'],
        data: FollowStatusResponseData.fromJson(
          userFollowStatusResponse.data['data'],
        ),
      ),
    );

    return result.data!;
  }
}
