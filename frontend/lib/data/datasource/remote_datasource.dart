import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/utils/dios/auth_dio.dart';

class RemoteDataSource {
  Future<void> getProfileData(BuildContext context) async {
    var dio = await authDio(context);
    // profile 응답을 위한 API Get 요청
    final profileResponse = await dio.get(getApi(API.getUserProfile));

    log('profile Get 요청 응답값: $profileResponse');
  }
}
