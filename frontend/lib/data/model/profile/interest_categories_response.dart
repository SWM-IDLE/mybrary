class InterestCategoriesResponse {
  String status;
  String message;
  InterestCategoriesResponseData data;

  InterestCategoriesResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory InterestCategoriesResponse.fromJson(Map<String, dynamic> json) {
    return InterestCategoriesResponse(
      status: json['status'],
      message: json['message'],
      data: InterestCategoriesResponseData.fromJson(json['data']),
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

class InterestCategoriesResponseData {
  List<InterestCategories>? interestCategories;

  InterestCategoriesResponseData({
    this.interestCategories,
  });

  InterestCategoriesResponseData.fromJson(Map<String, dynamic> json) {
    if (json['interestCategories'] != null) {
      interestCategories = <InterestCategories>[];
      json['interestCategories'].forEach((v) {
        interestCategories!.add(InterestCategories.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    if (interestCategories != null) {
      data['interestCategories'] =
          interestCategories!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class InterestCategories {
  int? id;
  String? name;
  String? description;
  List<CategoriesResponses>? interestResponses;

  InterestCategories({
    this.id,
    this.name,
    this.description,
    this.interestResponses,
  });

  InterestCategories.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    description = json['description'];
    if (json['interestResponses'] != null) {
      interestResponses = <CategoriesResponses>[];
      json['interestResponses'].forEach((v) {
        interestResponses!.add(CategoriesResponses.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['name'] = name;
    data['description'] = description;
    if (interestResponses != null) {
      data['interestResponses'] =
          interestResponses!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CategoriesResponses {
  int? id;
  String? name;

  CategoriesResponses({
    this.id,
    this.name,
  });

  CategoriesResponses.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id;
    data['name'] = name;
    return data;
  }
}
