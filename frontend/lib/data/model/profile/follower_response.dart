class FollowerResponse {
  String status;
  String message;
  FollowerResponseData? data;

  FollowerResponse({
    required this.status,
    required this.message,
    this.data,
  });

  factory FollowerResponse.fromJson(Map<String, dynamic> json) {
    return FollowerResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? FollowerResponseData.fromJson(json['data'])
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

class FollowerResponseData {
  String? userId;
  List<Followers>? followers;

  FollowerResponseData({required this.userId, this.followers});

  factory FollowerResponseData.fromJson(Map<String, dynamic> json) {
    return FollowerResponseData(
      userId: json['userId'],
      followers: json['followers'] != null
          ? (json['followers'] as List)
              .map((i) => Followers.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    if (followers != null) {
      data['followers'] = followers!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Followers {
  String? userId;
  String? nickname;
  String? profileImageUrl;

  Followers({
    this.userId,
    this.nickname,
    this.profileImageUrl,
  });

  Followers.fromJson(Map<String, dynamic> json) {
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
