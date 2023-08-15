import 'package:mybrary/data/model/book/mybook_detail_response.dart';

class MyBookRecordResponse {
  String status;
  String message;
  MyBookRecordResponseData? data;

  MyBookRecordResponse({
    required this.status,
    required this.message,
    required this.data,
  });

  factory MyBookRecordResponse.fromJson(Map<String, dynamic> json) {
    return MyBookRecordResponse(
      status: json['status'],
      message: json['message'],
      data: json['data'] != null
          ? MyBookRecordResponseData.fromJson(json['data'])
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

class MyBookRecordResponseData {
  String? readStatus;
  String? startDateOfPossession;
  bool? showable;
  bool? exchangeable;
  bool? shareable;
  MeaningTag? meaningTag;

  MyBookRecordResponseData({
    this.readStatus,
    this.startDateOfPossession,
    this.showable,
    this.exchangeable,
    this.shareable,
    this.meaningTag,
  });

  factory MyBookRecordResponseData.fromJson(Map<String, dynamic> json) {
    return MyBookRecordResponseData(
      readStatus: json['readStatus'],
      startDateOfPossession: json['startDateOfPossession'],
      showable: json['showable'],
      exchangeable: json['exchangeable'],
      shareable: json['shareable'],
      meaningTag: json['meaningTag'] != null
          ? MeaningTag.fromJson(json['meaningTag'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['readStatus'] = readStatus;
    data['startDateOfPossession'] = startDateOfPossession;
    data['showable'] = showable;
    data['exchangeable'] = exchangeable;
    data['shareable'] = shareable;
    if (meaningTag != null) {
      data['meaningTag'] = meaningTag!.toJson();
    }
    return data;
  }
}
