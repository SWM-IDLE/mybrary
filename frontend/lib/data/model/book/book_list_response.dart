class BookListResponse {
  String status;
  String message;
  List<BookListResponseData>? data;

  BookListResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookListResponse.fromJson(Map<String, dynamic> json) {
    return BookListResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? (json['data'] as List)
              .map((i) => BookListResponseData.fromJson(i))
              .toList()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (this.data != null) {
      data['data'] = this.data!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class BookListResponseData {
  int? id;
  String? myBookId;
  String? title;
  String? isbn13;
  String? thumbnailUrl;
  String? author;

  BookListResponseData({
    this.id,
    this.myBookId,
    this.title,
    this.isbn13,
    this.thumbnailUrl,
    this.author,
  });

  factory BookListResponseData.fromJson(Map<String, dynamic> json) {
    return BookListResponseData(
      id: json['id'],
      myBookId: json['myBookId'],
      title: json['title'],
      isbn13: json['isbn13'],
      thumbnailUrl: json['thumbnailUrl'],
      author: json['author'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['myBookId'] = myBookId;
    data['title'] = title;
    data['isbn13'] = isbn13;
    data['thumbnailUrl'] = thumbnailUrl;
    data['author'] = author;
    return data;
  }
}
