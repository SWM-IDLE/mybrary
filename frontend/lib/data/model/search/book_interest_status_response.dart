class BookInterestStatusResponse {
  String status;
  String message;
  BookInterestStatusResponseData? data;

  BookInterestStatusResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookInterestStatusResponse.fromJson(Map<String, dynamic> json) {
    return BookInterestStatusResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? BookInterestStatusResponseData.fromJson(json['data'])
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

class BookInterestStatusResponseData {
  bool? interested;

  BookInterestStatusResponseData({this.interested});

  factory BookInterestStatusResponseData.fromJson(Map<String, dynamic> json) {
    return BookInterestStatusResponseData(
      interested: json['interested'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['interested'] = interested;
    return data;
  }
}
