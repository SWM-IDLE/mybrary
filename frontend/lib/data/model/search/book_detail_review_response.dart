class BookDetailReviewResponse {
  String status;
  String message;
  BookDetailReviewResponseData? data;

  BookDetailReviewResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookDetailReviewResponse.fromJson(Map<String, dynamic> json) {
    return BookDetailReviewResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? BookDetailReviewResponseData.fromJson(json['data'])
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

class BookDetailReviewResponseData {
  String? title;
  String? isbn13;
  int? reviewCount;
  double? starRatingAverage;
  List<MyBookReviewList>? myBookReviewList;

  BookDetailReviewResponseData({
    this.title,
    this.isbn13,
    this.reviewCount,
    this.starRatingAverage,
    this.myBookReviewList,
  });

  factory BookDetailReviewResponseData.fromJson(Map<String, dynamic> json) {
    return BookDetailReviewResponseData(
      title: json['title'],
      isbn13: json['isbn13'],
      reviewCount: json['reviewCount'],
      starRatingAverage: json['starRatingAverage'],
      myBookReviewList: json['myBookReviewList'] != null
          ? (json['myBookReviewList'] as List)
              .map((i) => MyBookReviewList.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['title'] = title;
    data['isbn13'] = isbn13;
    data['reviewCount'] = reviewCount;
    data['starRatingAverage'] = starRatingAverage;
    if (myBookReviewList != null) {
      data['myBookReviewList'] =
          myBookReviewList!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class MyBookReviewList {
  int? id;
  String? userId;
  String? userNickname;
  String? userPictureUrl;
  String? content;
  double? starRating;
  String? createdAt;

  MyBookReviewList({
    this.id,
    this.userId,
    this.userNickname,
    this.userPictureUrl,
    this.content,
    this.starRating,
    this.createdAt,
  });

  factory MyBookReviewList.fromJson(Map<String, dynamic> json) {
    return MyBookReviewList(
      id: json['id'],
      userId: json['userId'],
      userNickname: json['userNickname'],
      userPictureUrl: json['userPictureUrl'],
      content: json['content'],
      starRating: json['starRating'],
      createdAt: json['createdAt'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['userId'] = userId;
    data['userNickname'] = userNickname;
    data['userPictureUrl'] = userPictureUrl;
    data['content'] = content;
    data['starRating'] = starRating;
    data['createdAt'] = createdAt;
    return data;
  }
}
