import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';

AppBar subPageAppBar({
  required String appBarTitle,
}) {
  return AppBar(
    elevation: 0,
    title: Text(appBarTitle),
    titleTextStyle: appBarTitleStyle.copyWith(
      fontSize: 18.0,
    ),
    centerTitle: true,
    backgroundColor: WHITE_COLOR,
    foregroundColor: BLACK_COLOR,
  );
}
