class BookListByCategoryResponse {
  String status;
  String message;
  BookListByCategoryResponseData? data;

  BookListByCategoryResponse({
    required this.status,
    required this.message,
    this.data,
  });

  factory BookListByCategoryResponse.fromJson(Map<String, dynamic> json) {
    return BookListByCategoryResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? BookListByCategoryResponseData.fromJson(json['data'])
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

class BookListByCategoryResponseData {
  List<Books>? books;
  String? nextRequestUrl;

  BookListByCategoryResponseData({this.books, this.nextRequestUrl});

  factory BookListByCategoryResponseData.fromJson(Map<String, dynamic> json) {
    return BookListByCategoryResponseData(
      books: json['books'] != null
          ? (json['books'] as List).map((i) => Books.fromJson(i)).toList()
          : null,
      nextRequestUrl: json['nextRequestUrl'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    if (books != null) {
      data['books'] = books!.map((v) => v.toJson()).toList();
    }
    data['nextRequestUrl'] = nextRequestUrl;
    return data;
  }
}

class Books {
  String? thumbnailUrl;
  String? isbn13;

  Books({
    this.thumbnailUrl,
    this.isbn13,
  });

  factory Books.fromJson(Map<String, dynamic> json) {
    return Books(
      thumbnailUrl: json['thumbnailUrl'],
      isbn13: json['isbn13'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['thumbnailUrl'] = thumbnailUrl;
    data['isbn13'] = isbn13;
    return data;
  }
}
