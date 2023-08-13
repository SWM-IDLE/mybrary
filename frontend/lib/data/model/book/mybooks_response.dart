class MyBooksResponse {
  String status;
  String message;
  List<MyBooksResponseData>? data;

  MyBooksResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyBooksResponse.fromJson(Map<String, dynamic> json) {
    return MyBooksResponse(
      status: json['status'],
      message: json['message'],
      data: List<MyBooksResponseData>.from(
        json['data'].map(
          (x) => MyBooksResponseData.fromJson(x),
        ),
      ),
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    data['data'] = this.data!.map((v) => v.toJson()).toList();
    return data;
  }
}

class MyBooksResponseData {
  int? id;
  bool? showable;
  bool? exchangeable;
  bool? shareable;
  String? readStatus;
  String? startDateOfPossession;
  BookInfo? book;

  MyBooksResponseData({
    this.id,
    this.showable,
    this.exchangeable,
    this.shareable,
    this.readStatus,
    this.startDateOfPossession,
    this.book,
  });

  MyBooksResponseData.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    showable = json['showable'];
    exchangeable = json['exchangeable'];
    shareable = json['shareable'];
    readStatus = json['readStatus'];
    startDateOfPossession = json['startDateOfPossession'];
    book = json['book'] != null ? BookInfo.fromJson(json['book']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['showable'] = showable;
    data['exchangeable'] = exchangeable;
    data['shareable'] = shareable;
    data['readStatus'] = readStatus;
    data['startDateOfPossession'] = startDateOfPossession;
    if (book != null) {
      data['book'] = book!.toJson();
    }
    return data;
  }
}

class BookInfo {
  int? id;
  String? title;
  String? description;
  String? thumbnailUrl;
  double? starRating;
  String? authors;

  BookInfo({
    this.id,
    this.title,
    this.description,
    this.thumbnailUrl,
    this.starRating,
    this.authors,
  });

  factory BookInfo.fromJson(Map<String, dynamic> json) {
    return BookInfo(
      id: json['id'],
      title: json['title'],
      description: json['description'],
      thumbnailUrl: json['thumbnailUrl'],
      starRating: json['starRating'],
      authors: json['authors'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['title'] = title;
    data['description'] = description;
    data['thumbnailUrl'] = thumbnailUrl;
    data['starRating'] = starRating;
    return data;
  }
}
