class BookSearchDetailResponse {
  String status;
  String message;
  BookSearchDetailResponseData data;

  BookSearchDetailResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookSearchDetailResponse.fromJson(Map<String, dynamic> json) {
    return BookSearchDetailResponse(
      status: json['status'],
      message: json['message'],
      data: BookSearchDetailResponseData.fromJson(json['data']),
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

class BookSearchDetailResponseData {
  String? title;
  String? subTitle;
  String? thumbnail;
  String? link;
  String? author;
  List<Authors>? authors;
  List<Translators>? translators;
  double? starRating;
  int? reviewCount;
  String? publicationDate;
  String? category;
  int? categoryId;
  int? pages;
  String? publisher;
  String? description;
  String? toc;
  String? isbn10;
  String? isbn13;
  int? weight;
  int? sizeDepth;
  int? sizeHeight;
  int? sizeWidth;
  int? priceSales;
  int? priceStandard;
  int? holderCount;
  int? readCount;
  int? interestCount;

  BookSearchDetailResponseData({
    this.title,
    this.subTitle,
    this.thumbnail,
    this.link,
    this.author,
    this.authors,
    this.translators,
    this.starRating,
    this.reviewCount,
    this.publicationDate,
    this.category,
    this.categoryId,
    this.pages,
    this.publisher,
    this.description,
    this.toc,
    this.isbn10,
    this.isbn13,
    this.weight,
    this.sizeDepth,
    this.sizeHeight,
    this.sizeWidth,
    this.priceSales,
    this.priceStandard,
    this.holderCount,
    this.readCount,
    this.interestCount,
  });

  BookSearchDetailResponseData.fromJson(Map<String, dynamic> json) {
    title = json['title'];
    subTitle = json['subTitle'];
    thumbnail = json['thumbnail'] ?? '';
    link = json['link'];
    author = json['author'];
    if (json['authors'] != null) {
      authors = <Authors>[];
      json['authors'].forEach((v) {
        authors!.add(Authors.fromJson(v));
      });
    }
    if (json['translators'] != null) {
      translators = <Translators>[];
      json['translators'].forEach((v) {
        translators!.add(Translators.fromJson(v));
      });
    }
    starRating = json['starRating'];
    reviewCount = json['reviewCount'];
    publicationDate = json['publicationDate'];
    category = json['category'];
    categoryId = json['categoryId'];
    pages = json['pages'];
    publisher = json['publisher'];
    description = json['description'];
    toc = json['toc'];
    isbn10 = json['isbn10'];
    isbn13 = json['isbn13'];
    weight = json['weight'];
    sizeDepth = json['sizeDepth'];
    sizeHeight = json['sizeHeight'];
    sizeWidth = json['sizeWidth'];
    priceSales = json['priceSales'];
    priceStandard = json['priceStandard'];
    holderCount = json['holderCount'];
    readCount = json['readCount'];
    interestCount = json['interestCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['title'] = title;
    data['subTitle'] = subTitle;
    data['thumbnail'] = thumbnail;
    data['link'] = link;
    data['author'] = author;
    if (authors != null) {
      data['authors'] = authors!.map((v) => v.toJson()).toList();
    }
    if (translators != null) {
      data['translators'] = translators!.map((v) => v.toJson()).toList();
    }
    data['starRating'] = starRating;
    data['reviewCount'] = reviewCount;
    data['publicationDate'] = publicationDate;
    data['category'] = category;
    data['categoryId'] = categoryId;
    data['pages'] = pages;
    data['publisher'] = publisher;
    data['description'] = description;
    data['toc'] = toc;
    data['isbn10'] = isbn10;
    data['isbn13'] = isbn13;
    data['weight'] = weight;
    data['sizeDepth'] = sizeDepth;
    data['sizeHeight'] = sizeHeight;
    data['sizeWidth'] = sizeWidth;
    data['priceSales'] = priceSales;
    data['priceStandard'] = priceStandard;
    data['holderCount'] = holderCount;
    data['readCount'] = readCount;
    data['interestCount'] = interestCount;
    return data;
  }
}

class Authors {
  String? name;
  int? authorId;

  Authors({
    this.name,
    this.authorId,
  });

  Authors.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    authorId = json['authorId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['authorId'] = authorId;
    return data;
  }
}

class Translators {
  String? name;
  int? translatorId;

  Translators({
    this.name,
    this.translatorId,
  });

  Translators.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    translatorId = json['translatorId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['translatorId'] = translatorId;
    return data;
  }
}
