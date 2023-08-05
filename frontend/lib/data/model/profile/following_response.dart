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
  String requestLoginId;
  List<Followings>? followings;

  FollowingResponseData({
    required this.requestLoginId,
    this.followings,
  });

  factory FollowingResponseData.fromJson(Map<String, dynamic> json) {
    return FollowingResponseData(
      requestLoginId: json['requestLoginId'],
      followings: json['followings'] != null
          ? (json['followings'] as List)
              .map((i) => Followings.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['requestLoginId'] = requestLoginId;
    if (followings != null) {
      data['followings'] = followings!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Followings {
  int? id;
  String? loginId;
  String? nickname;
  String? profileImageUrl;

  Followings({this.id, this.loginId, this.nickname, this.profileImageUrl});

  Followings.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    loginId = json['loginId'];
    nickname = json['nickname'];
    profileImageUrl = json['profileImageUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['loginId'] = loginId;
    data['nickname'] = nickname;
    data['profileImageUrl'] = profileImageUrl;
    return data;
  }
}
