class BookCompletedStatusResponse {
  String status;
  String message;
  BookCompletedStatusResponseData? data;

  BookCompletedStatusResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory BookCompletedStatusResponse.fromJson(Map<String, dynamic> json) {
    return BookCompletedStatusResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? BookCompletedStatusResponseData.fromJson(json['data'])
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

class BookCompletedStatusResponseData {
  bool? completed;

  BookCompletedStatusResponseData({this.completed});

  factory BookCompletedStatusResponseData.fromJson(Map<String, dynamic> json) {
    return BookCompletedStatusResponseData(
      completed: json['completed'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['completed'] = completed;
    return data;
  }
}
