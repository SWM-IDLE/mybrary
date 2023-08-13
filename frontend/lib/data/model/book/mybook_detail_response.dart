class MyBookDetailResponse {
  String status;
  String message;
  MyBookDetailResponseData? data;

  MyBookDetailResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyBookDetailResponse.fromJson(Map<String, dynamic> json) {
    return MyBookDetailResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? MyBookDetailResponseData.fromJson(json['data'])
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

class MyBookDetailResponseData {
  int? id;
  bool? showable;
  bool? exchangeable;
  bool? shareable;
  String? readStatus;
  String? startDateOfPossession;
  MeaningTag? meaningTag;
  Book? book;

  MyBookDetailResponseData({
    this.id,
    this.showable,
    this.exchangeable,
    this.shareable,
    this.readStatus,
    this.startDateOfPossession,
    this.meaningTag,
    this.book,
  });

  factory MyBookDetailResponseData.fromJson(Map<String, dynamic> json) {
    return MyBookDetailResponseData(
      id: json['id'],
      showable: json['showable'],
      exchangeable: json['exchangeable'],
      shareable: json['shareable'],
      readStatus: json['readStatus'],
      startDateOfPossession: json['startDateOfPossession'],
      meaningTag: json['meaningTag'] != null
          ? MeaningTag.fromJson(json['meaningTag'])
          : null,
      book: json['book'] != null ? Book.fromJson(json['book']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['showable'] = showable;
    data['exchangeable'] = exchangeable;
    data['shareable'] = shareable;
    data['readStatus'] = readStatus;
    data['startDateOfPossession'] = startDateOfPossession;
    if (meaningTag != null) {
      data['meaningTag'] = meaningTag!.toJson();
    }
    if (book != null) {
      data['book'] = book!.toJson();
    }
    return data;
  }
}

class MeaningTag {
  String? quote;
  String? colorCode;

  MeaningTag({
    this.quote,
    this.colorCode,
  });

  factory MeaningTag.fromJson(Map<String, dynamic> json) {
    return MeaningTag(
      quote: json['quote'],
      colorCode: json['colorCode'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['quote'] = quote;
    data['colorCode'] = colorCode;
    return data;
  }
}

class Book {
  int? id;
  String? title;
  String? description;
  List<String>? authors;
  List<String>? translators;
  String? publisher;
  String? thumbnailUrl;
  double? starRating;

  Book({
    this.id,
    this.title,
    this.description,
    this.authors,
    this.translators,
    this.publisher,
    this.thumbnailUrl,
    this.starRating,
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      id: json['id'],
      title: json['title'],
      description: json['description'],
      authors:
          json['authors'] != null ? List<String>.from(json['authors']) : null,
      translators: json['translators'] != null
          ? List<String>.from(json['translators'])
          : null,
      publisher: json['publisher'],
      thumbnailUrl: json['thumbnailUrl'],
      starRating: json['starRating'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['title'] = title;
    data['description'] = description;
    data['authors'] = authors;
    data['translators'] = translators;
    data['publisher'] = publisher;
    data['thumbnailUrl'] = thumbnailUrl;
    data['starRating'] = starRating;
    return data;
  }
}
