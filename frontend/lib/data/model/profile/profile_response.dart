class ProfileResponse {
  String status;
  String message;
  ProfileResponseData? data;

  ProfileResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory ProfileResponse.fromJson(Map<String, dynamic> json) {
    return ProfileResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? ProfileResponseData.fromJson(json['data'])
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

class ProfileResponseData {
  String? nickname;
  String? profileImageUrl;
  String? introduction;

  ProfileResponseData({
    required this.nickname,
    required this.profileImageUrl,
    required this.introduction,
  });

  factory ProfileResponseData.fromJson(Map<String, dynamic> json) {
    return ProfileResponseData(
      nickname: json['nickname'],
      profileImageUrl: json['profileImageUrl'],
      introduction: json['introduction'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['nickname'] = nickname;
    data['profileImageUrl'] = profileImageUrl;
    data['introduction'] = introduction;
    return data;
  }
}
