class MyBookReviewResponse {
  String status;
  String message;
  MyBookReviewResponseData? data;

  MyBookReviewResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyBookReviewResponse.fromJson(Map<String, dynamic> json) {
    return MyBookReviewResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? MyBookReviewResponseData.fromJson(json['data'])
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

class MyBookReviewResponseData {
  int? id;
  String? content;
  double? starRating;
  String? createdAt;
  String? updatedAt;

  MyBookReviewResponseData({
    this.id,
    this.content,
    this.starRating,
    this.createdAt,
    this.updatedAt,
  });

  factory MyBookReviewResponseData.fromJson(Map<String, dynamic> json) {
    return MyBookReviewResponseData(
      id: json['id'],
      content: json['content'],
      starRating: json['starRating'],
      createdAt: json['createdAt'],
      updatedAt: json['updatedAt'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['content'] = content;
    data['starRating'] = starRating;
    data['createdAt'] = createdAt;
    data['updatedAt'] = updatedAt;
    return data;
  }
}

class MyBookReviewUpdateResponse {
  String status;
  String message;
  MyBookReviewUpdateResponseData? data;

  MyBookReviewUpdateResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyBookReviewUpdateResponse.fromJson(Map<String, dynamic> json) {
    return MyBookReviewUpdateResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? MyBookReviewUpdateResponseData.fromJson(json['data'])
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

class MyBookReviewUpdateResponseData {
  int? id;
  String? content;
  double? starRating;

  MyBookReviewUpdateResponseData({
    this.id,
    this.content,
    this.starRating,
  });

  factory MyBookReviewUpdateResponseData.fromJson(Map<String, dynamic> json) {
    return MyBookReviewUpdateResponseData(
      id: json['id'],
      content: json['content'],
      starRating: json['starRating'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['content'] = content;
    data['starRating'] = starRating;
    return data;
  }
}
