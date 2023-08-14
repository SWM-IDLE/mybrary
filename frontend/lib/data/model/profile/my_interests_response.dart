class MyInterestsResponse {
  String status;
  String message;
  MyInterestsResponseData? data;

  MyInterestsResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyInterestsResponse.fromJson(Map<String, dynamic> json) {
    return MyInterestsResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? MyInterestsResponseData.fromJson(json['data'])
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

class MyInterestsResponseData {
  String? userId;
  List<UserInterests>? userInterests;

  MyInterestsResponseData({
    required this.userId,
    required this.userInterests,
  });

  factory MyInterestsResponseData.fromJson(Map<String, dynamic> json) {
    return MyInterestsResponseData(
      userId: json['userId'],
      userInterests: json['userInterests'] != null
          ? (json['userInterests'] as List)
              .map((i) => UserInterests.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
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
