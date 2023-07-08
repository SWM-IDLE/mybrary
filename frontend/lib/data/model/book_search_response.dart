import 'package:mybrary/data/model/book_search_data.dart';

class BookSearchResponse {
  String status;
  String message;
  BookSearchResponseData? data;

  BookSearchResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookSearchResponse.fromJson(Map<String, dynamic> json) {
    return BookSearchResponse(
        status: json['status'],
        message: json['message'],
        data: json['data'] != null
            ? BookSearchResponseData.fromJson(json['data'])
            : null);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    data['data'] = this.data!.toJson();
    return data;
  }
}

class BookSearchResponseData {
  List<BookSearchData>? bookSearchResult;
  String? nextRequestUrl;

  BookSearchResponseData({this.bookSearchResult, this.nextRequestUrl});

  BookSearchResponseData.fromJson(Map<String, dynamic> json) {
    if (json['bookSearchResult'] != null) {
      bookSearchResult = <BookSearchData>[];
      json['bookSearchResult'].forEach((v) {
        bookSearchResult!.add(BookSearchData.fromJson(v));
      });
    }
    nextRequestUrl = json['nextRequestUrl'];
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
