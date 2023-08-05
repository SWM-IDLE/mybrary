import 'package:mybrary/data/datasource/profile/follow_datasource.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';

class FollowRepository {
  final FollowDataSource _followDataSource = FollowDataSource();

  Future<FollowerResponseData> getFollower({
    required String userId,
  }) {
    return _followDataSource.getFollower(userId);
  }

  Future<FollowingResponseData> getFollowings({
    required String userId,
  }) {
    return _followDataSource.getFollowings(userId);
  }

  Future<FollowingResponseData?> updateFollowing({
    required String userId,
    required String targetId,
  }) {
    return _followDataSource.updateFollowing(userId, targetId);
  }

  Future<FollowingResponseData?> deleteFollowing({
    required String userId,
    required String targetId,
  }) {
    return _followDataSource.deleteFollowing(userId, targetId);
  }

  Future<FollowerResponseData?> deleteFollower({
    required String userId,
    required String sourceId,
  }) {
    return _followDataSource.deleteFollower(userId, sourceId);
  }
}
