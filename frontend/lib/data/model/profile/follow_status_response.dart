class FollowStatusResponse {
  String status;
  String message;
  FollowStatusResponseData? data;

  FollowStatusResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory FollowStatusResponse.fromJson(Map<String, dynamic> json) {
    return FollowStatusResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? FollowStatusResponseData.fromJson(json['data'])
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

class FollowStatusResponseData {
  String? userId;
  String? targetId;
  bool? following;

  FollowStatusResponseData({this.userId, this.targetId, this.following});

  factory FollowStatusResponseData.fromJson(Map<String, dynamic> json) {
    return FollowStatusResponseData(
      userId: json['userId'],
      targetId: json['targetId'],
      following: json['following'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    data['targetId'] = targetId;
    data['following'] = following;
    return data;
  }
}
