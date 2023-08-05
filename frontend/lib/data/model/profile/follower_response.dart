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
  String requestLoginId;
  List<Followers>? followers;

  FollowerResponseData({required this.requestLoginId, this.followers});

  factory FollowerResponseData.fromJson(Map<String, dynamic> json) {
    return FollowerResponseData(
      requestLoginId: json['requestLoginId'],
      followers: json['followers'] != null
          ? (json['followers'] as List)
              .map((i) => Followers.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['requestLoginId'] = requestLoginId;
    if (followers != null) {
      data['followers'] = followers!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Followers {
  int? id;
  String? loginId;
  String? nickname;
  String? profileImageUrl;

  Followers({
    this.id,
    this.loginId,
    this.nickname,
    this.profileImageUrl,
  });

  Followers.fromJson(Map<String, dynamic> json) {
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
