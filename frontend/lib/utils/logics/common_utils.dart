import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

Widget loadingIndicator() {
  return CircularProgressIndicator(
    backgroundColor: primaryColor.withOpacity(0.2),
    valueColor: const AlwaysStoppedAnimation<Color>(primaryColor),
  );
}

Widget buildErrorPage({String? message}) {
  return Center(
    child: SizedBox(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const Icon(Icons.wifi_off_rounded, color: grey777777),
          const SizedBox(height: 16.0),
          Text(
            message ?? '정보를 가져올 수 없습니다.\n인터넷 연결 상태를 확인해주세요.',
            textAlign: TextAlign.center,
            style: const TextStyle(height: 1.2, color: grey777777),
          ),
        ],
      ),
    ),
  );
}

SliverAppBar commonSliverAppBar({
  required String appBarTitle,
  List<Widget>? appBarActions,
}) {
  return SliverAppBar(
    elevation: 0,
    title: Text(appBarTitle),
    titleTextStyle: appBarTitleStyle.copyWith(
      fontSize: 16.0,
    ),
    pinned: true,
    centerTitle: true,
    backgroundColor: commonWhiteColor,
    foregroundColor: commonBlackColor,
    actions: appBarActions,
  );
}
