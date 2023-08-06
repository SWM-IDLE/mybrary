class BookSearchResponse {
  String status;
  String message;
  BookSearchResponseData data;

  BookSearchResponse(
      {required this.status, required this.message, required this.data});

  factory BookSearchResponse.fromJson(Map<String, dynamic> json) {
    return BookSearchResponse(
      status: json['status'],
      message: json['message'],
      data: BookSearchResponseData.fromJson(json['data']),
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

class BookSearchResponseData {
  List<BookSearchResult>? bookSearchResult;
  String? nextRequestUrl;

  BookSearchResponseData({
    this.bookSearchResult,
    this.nextRequestUrl,
  });

  factory BookSearchResponseData.fromJson(Map<String, dynamic> json) {
    return BookSearchResponseData(
      bookSearchResult: json['bookSearchResult'] != null
          ? (json['bookSearchResult'] as List)
              .map((i) => BookSearchResult.fromJson(i))
              .toList()
          : null,
      nextRequestUrl: json['nextRequestUrl'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    if (bookSearchResult != null) {
      data['bookSearchResult'] =
          bookSearchResult!.map((v) => v.toJson()).toList();
    }
    data['nextRequestUrl'] = nextRequestUrl;
    return data;
  }
}

class BookSearchResult {
  String? title;
  String? description;
  String? author;
  String? isbn13;
  String? thumbnailUrl;
  String? publicationDate;
  double? starRating;

  BookSearchResult({
    this.title,
    this.description,
    this.author,
    this.isbn13,
    this.thumbnailUrl,
    this.publicationDate,
    this.starRating,
  });

  factory BookSearchResult.fromJson(Map<String, dynamic> json) {
    return BookSearchResult(
      title: json['title'],
      description: json['description'],
      author: json['author'],
      isbn13: json['isbn13'],
      thumbnailUrl: json['thumbnailUrl'],
      publicationDate: json['publicationDate'],
      starRating: json['starRating'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['title'] = title;
    data['description'] = description;
    data['author'] = author;
    data['isbn13'] = isbn13;
    data['thumbnailUrl'] = thumbnailUrl;
    data['publicationDate'] = publicationDate;
    data['starRating'] = starRating;
    return data;
  }
}
