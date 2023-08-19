class UserSearchResponse {
  String status;
  String message;
  UserSearchResponseData? data;

  UserSearchResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory UserSearchResponse.fromJson(Map<String, dynamic> json) {
    return UserSearchResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? UserSearchResponseData.fromJson(json['data'])
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

class UserSearchResponseData {
  List<SearchedUsers>? searchedUsers;

  UserSearchResponseData({this.searchedUsers});

  factory UserSearchResponseData.fromJson(Map<String, dynamic> json) {
    return UserSearchResponseData(
      searchedUsers: json['searchedUsers'] != null
          ? (json['searchedUsers'] as List)
              .map((i) => SearchedUsers.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    if (searchedUsers != null) {
      data['searchedUsers'] = searchedUsers!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class SearchedUsers {
  String? userId;
  String? nickname;
  String? profileImageUrl;

  SearchedUsers({this.userId, this.nickname, this.profileImageUrl});

  factory SearchedUsers.fromJson(Map<String, dynamic> json) {
    return SearchedUsers(
      userId: json['userId'],
      nickname: json['nickname'],
      profileImageUrl: json['profileImageUrl'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    data['nickname'] = nickname;
    data['profileImageUrl'] = profileImageUrl;
    return data;
  }
}
