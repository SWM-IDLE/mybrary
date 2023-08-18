class BookRegisteredStatusResponse {
  String status;
  String message;
  BookRegisteredStatusResponseData? data;

  BookRegisteredStatusResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookRegisteredStatusResponse.fromJson(Map<String, dynamic> json) {
    return BookRegisteredStatusResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? BookRegisteredStatusResponseData.fromJson(json['data'])
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

class BookRegisteredStatusResponseData {
  bool? registered;

  BookRegisteredStatusResponseData({this.registered});

  factory BookRegisteredStatusResponseData.fromJson(Map<String, dynamic> json) {
    return BookRegisteredStatusResponseData(
      registered: json['registered'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['registered'] = registered;
    return data;
  }
}
