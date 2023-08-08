class InterestBookResponse {
  String status;
  String message;
  InterestBookResponseData data;

  InterestBookResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory InterestBookResponse.fromJson(Map<String, dynamic> json) {
    return InterestBookResponse(
      status: json['status'],
      message: json['message'],
      data: InterestBookResponseData.fromJson(json['data']),
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

class InterestBookResponseData {
  String? userId;
  String? isbn13;
  bool? interested;

  InterestBookResponseData({
    this.userId,
    this.isbn13,
    this.interested,
  });

  factory InterestBookResponseData.fromJson(Map<String, dynamic> json) {
    return InterestBookResponseData(
      userId: json['userId'],
      isbn13: json['isbn13'],
      interested: json['interested'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['userId'] = userId;
    data['isbn13'] = isbn13;
    data['interested'] = interested;
    return data;
  }
}
