import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/profile/follow_datasource.dart';
import 'package:mybrary/data/model/profile/follow_status_response.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';

class FollowRepository {
  final FollowDataSource _followDataSource = FollowDataSource();

  Future<FollowerResponseData> getFollower({
    required BuildContext context,
    required String userId,
  }) {
    return _followDataSource.getFollower(context, userId);
  }

  Future<FollowingResponseData> getFollowings({
    required BuildContext context,
    required String userId,
  }) {
    return _followDataSource.getFollowings(context, userId);
  }

  Future<FollowingResponseData?> updateFollowing({
    required BuildContext context,
    required String userId,
    required String targetId,
  }) {
    return _followDataSource.updateFollowing(context, userId, targetId);
  }

  Future<FollowingResponseData?> deleteFollowing({
    required BuildContext context,
    required String userId,
    required String targetId,
  }) {
    return _followDataSource.deleteFollowing(context, userId, targetId);
  }

  Future<FollowerResponseData?> deleteFollower({
    required BuildContext context,
    required String userId,
    required String sourceId,
  }) {
    return _followDataSource.deleteFollower(context, userId, sourceId);
  }

  Future<FollowStatusResponseData> getUserFollowStatus({
    required BuildContext context,
    required String userId,
    required String targetId,
  }) {
    return _followDataSource.getUserFollowStatus(context, userId, targetId);
  }
}
