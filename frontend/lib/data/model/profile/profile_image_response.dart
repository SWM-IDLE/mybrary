class ProfileImageResponse {
  String status;
  String message;
  ProfileImageResponseData? data;

  ProfileImageResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory ProfileImageResponse.fromJson(Map<String, dynamic> json) {
    return ProfileImageResponse(
      status: json['status'],
      message: json['message'],
      data: ProfileImageResponseData.fromJson(json['data']),
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

class ProfileImageResponseData {
  String? profileImageUrl;

  ProfileImageResponseData({
    required this.profileImageUrl,
  });

  factory ProfileImageResponseData.fromJson(Map<String, dynamic> json) {
    return ProfileImageResponseData(
      profileImageUrl: json['profileImageUrl'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['profileImageUrl'] = profileImageUrl;
    return data;
  }
}
