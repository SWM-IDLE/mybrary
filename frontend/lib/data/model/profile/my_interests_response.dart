class MyInterestsResponse {
  String status;
  String message;
  MyInterestsResponseData data;

  MyInterestsResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyInterestsResponse.fromJson(Map<String, dynamic> json) {
    return MyInterestsResponse(
      status: json['status'],
      message: json['message'],
      data: MyInterestsResponseData.fromJson(json['data']),
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    data['data'] = this.data.toJson();
    return data;
  }
}

class MyInterestsResponseData {
  String? loginId;
  List<UserInterests>? userInterests;

  MyInterestsResponseData({
    required this.loginId,
    required this.userInterests,
  });

  MyInterestsResponseData.fromJson(Map<String, dynamic> json) {
    loginId = json['loginId'];
    if (json['userInterests'] != null) {
      userInterests = <UserInterests>[];
      json['userInterests'].forEach((v) {
        userInterests!.add(UserInterests.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['loginId'] = loginId;
    if (userInterests != null) {
      data['userInterests'] = userInterests!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class UserInterests {
  int? id;
  String? name;

  UserInterests({this.id, this.name});

  UserInterests.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['name'] = name;
    return data;
  }
}
