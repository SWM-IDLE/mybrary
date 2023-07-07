class SearchBookData {
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

  SearchBookData({
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

  factory SearchBookData.fromJson(Map<String, dynamic> json) {
    return SearchBookData(
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
          ? "https://lh3.googleusercontent.com/pw/AIL4fc9Qr-4uR85VSfAPvcbqjuy8lLD3iDAwwx2i2LKLLnIxkXzc12Bg4S9Okm5N9-48LaLyL7hGYmv0rW-Zl_02weLKoRU0e7YlbTKM5JTfQzs8TR0UVQUCnAn6EtT_SMOu5ZI52Oin2_oMvXyd6MCMe6qbBCJ3EehnotoY5pY-2AAmyVY484xW6bunB5yg8hQ_gK72Hzr2K2rTUjPVgkFr_BkN05dwbDNixXiEcPiHrjtKwocQzvS43KaPseDxFPI_SUsfZ2Jwsyuc_27GQxor2SsNetf-vXtElc0Qo1g1PHTLlLDmJt5Tne0dcJmzKxNWhGnO_qIQCrKhhRY0JwHU62lepZmk-BB4x52Wf2_4oEXiVL_daQtCH6JIR0tsp3x2V_o24rJKgm4SJrkmwKiDzAKIiQW3dY8EFyXjbUyaT8WKdmtH7zrccRi5EDjuFaWyzM38mId2I5shHW6voEQUwYXmMJ754OUoq8kZ23EnWUkJU4c_0HEcch0i3NP3U9apYeVJ1rVZtcsQcmMtEDb4tmRrfxzGTRvCeHKmZb-8g-FLDRglc7gmmVtmcZHXQrq2dC53y9QERp-Phzk-e3W45fPNtA63P3o1ZzPB_7gP_k7801ZcYdh6RJCnyWQMZf45o4YZxve1QeC_vHpMlobxgDxNZUoyK550XklUv2Yy_n5-ql3gqpHQK9ecuspL1TK340KqmrwlgCpoeXR-EBm_znA5lKfKnh-w8htZZtLEOpv0vZTFq8MPu9iNjoIQs28i5HOrhZjNH5yiysrh_VJyt6uzf_4HtZuhwyy8sf7a0NN89AZ1Sw3EhjwvD9B4eDySBmzD7Zb4MA_LDZQNsfrxPVa4t0alusHrWBpeB7dZYAIuzl41sZC1p7Jj89gKXkW5FMXQzwkWXSeQLmrZ4ifdDzk28DZFX8ERVVAhBRjRB1niKRIwW-hG9Bnb7W3JWLVpogBzhsH5xYAEVpHvg2VqGAuaPPRcrw=w290-h294-s-no?authuser=2"
          : json['thumbnailUrl'],
      publicationDate: json['publicationDate'],
      price: json['price'],
      salePrice: json['salePrice'],
      saleBookStatus: json['saleBookStatus'],
      starRating: json['starRating'],
    );
  }
}

List<SearchBookData> convertSearchBookDataList(List<dynamic> dataList) {
  return dataList.map((data) {
    return SearchBookData.fromJson(data as Map<String, dynamic>);
  }).toList();
}
