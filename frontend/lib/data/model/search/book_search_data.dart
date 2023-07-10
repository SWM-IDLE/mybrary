class BookSearchData {
  final String? title;
  final String? description;
  final String? detailsUrl;
  final String? isbn10;
  final String? isbn13;
  final List<String>? authors;
  final List<String>? translators;
  final String? publisher;
  final String? thumbnailUrl;
  final String? publicationDate;
  final int? price;
  final int? salePrice;
  final String? saleBookStatus;
  final double? starRating;

  BookSearchData({
    required this.title,
    required this.description,
    required this.detailsUrl,
    required this.isbn10,
    required this.isbn13,
    required this.authors,
    required this.translators,
    required this.publisher,
    required this.thumbnailUrl,
    required this.publicationDate,
    required this.price,
    required this.salePrice,
    required this.saleBookStatus,
    required this.starRating,
  });

  // thumbnailUrl이 null인 경우, 기본 썸네일 이미지 필요 (현재는 임시 썸네일)
  factory BookSearchData.fromJson(Map<String, dynamic> json) {
    return BookSearchData(
      title: json['title'],
      description: json['description'],
      detailsUrl: json['detailsUrl'],
      isbn10: json['isbn10'],
      isbn13: json['isbn13'],
      authors: json['authors'] == null
          ? []
          : List<String>.from(json["authors"].map((x) => x)),
      translators: json['translators'] == null
          ? []
          : List<String>.from(json["translators"].map((x) => x)),
      publisher: json['publisher'],
      thumbnailUrl: json['thumbnailUrl'] == ""
          ? "https://kuku-keke.com/wp-content/uploads/2020/05/2695_3.png"
          : json['thumbnailUrl'],
      publicationDate: json['publicationDate'],
      price: json['price'],
      salePrice: json['salePrice'],
      saleBookStatus: json['saleBookStatus'],
      starRating: json['starRating'],
    );
  }
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['title'] = title;
    data['description'] = description;
    data['detailsUrl'] = detailsUrl;
    data['isbn10'] = isbn10;
    data['isbn13'] = isbn13;
    data['authors'] = authors;
    data['translators'] = translators;
    data['publisher'] = publisher;
    data['thumbnailUrl'] = thumbnailUrl;
    data['publicationDate'] = publicationDate;
    data['price'] = price;
    data['salePrice'] = salePrice;
    data['saleBookStatus'] = saleBookStatus;
    data['starRating'] = starRating;
    return data;
  }
}
