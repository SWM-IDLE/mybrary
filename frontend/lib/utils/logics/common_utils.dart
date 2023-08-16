import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

Widget loadingIndicator() {
  return CircularProgressIndicator(
    backgroundColor: primaryColor.withOpacity(0.2),
    valueColor: const AlwaysStoppedAnimation<Color>(primaryColor),
  );
}
