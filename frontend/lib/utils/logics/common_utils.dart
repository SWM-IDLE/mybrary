import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

Widget loadingIndicator() {
  return SizedBox(
    width: 30,
    height: 30,
    child: CircularProgressIndicator(
      backgroundColor: primaryColor.withOpacity(0.2),
      valueColor: const AlwaysStoppedAnimation<Color>(primaryColor),
    ),
  );
}