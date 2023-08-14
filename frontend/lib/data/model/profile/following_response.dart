class FollowingResponse {
  String status;
  String message;
  FollowingResponseData? data;

  FollowingResponse({
    required this.status,
    required this.message,
    this.data,
  });

  factory FollowingResponse.fromJson(Map<String, dynamic> json) {
    return FollowingResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? FollowingResponseData.fromJson(json['data'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    return data;
  }
}

class FollowingResponseData {
  String? userId;
  List<Followings>? followings;

  FollowingResponseData({
    required this.userId,
    this.followings,
  });

  factory FollowingResponseData.fromJson(Map<String, dynamic> json) {
    return FollowingResponseData(
      userId: json['userId'],
      followings: json['followings'] != null
          ? (json['followings'] as List)
              .map((i) => Followings.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    if (followings != null) {
      data['followings'] = followings!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Followings {
  String? userId;
  String? nickname;
  String? profileImageUrl;

  Followings({
    this.userId,
    this.nickname,
    this.profileImageUrl,
  });

  Followings.fromJson(Map<String, dynamic> json) {
    userId = json['userId'];
    nickname = json['nickname'];
    profileImageUrl = json['profileImageUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    data['nickname'] = nickname;
    data['profileImageUrl'] = profileImageUrl;
    return data;
  }
}
