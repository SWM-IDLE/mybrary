class TodayRegisteredBookCountResponse {
  String status;
  String message;
  TodayRegisteredBookCountResponseData? data;

  TodayRegisteredBookCountResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory TodayRegisteredBookCountResponse.fromJson(Map<String, dynamic> json) {
    return TodayRegisteredBookCountResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? TodayRegisteredBookCountResponseData.fromJson(json['data'])
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

class TodayRegisteredBookCountResponseData {
  int? count;

  TodayRegisteredBookCountResponseData({
    this.count,
  });

  factory TodayRegisteredBookCountResponseData.fromJson(
      Map<String, dynamic> json) {
    return TodayRegisteredBookCountResponseData(
      count: json['count'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['count'] = count;
    return data;
  }
}
