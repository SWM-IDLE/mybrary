import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

const inputHintStyle = TextStyle(
  color: inputHintColor,
  fontSize: 14.0,
  fontWeight: FontWeight.w400,
);

const nameInputBorderStyle = UnderlineInputBorder(
  borderSide: BorderSide(
    color: inputBorderColor,
    width: 1,
  ),
);

const introInputBorderStyle = OutlineInputBorder(
  borderSide: BorderSide(
    color: inputBorderColor,
    width: 1,
  ),
);

const bottomSheetStyle = RoundedRectangleBorder(
  borderRadius: BorderRadius.only(
    topLeft: Radius.circular(20.0),
    topRight: Radius.circular(20.0),
  ),
);

const appBarTitleStyle = TextStyle(
  color: BLACK_COLOR,
  fontSize: 17.0,
  fontWeight: FontWeight.w700,
);

const profileEditTitleStyle = TextStyle(
  color: GREY_06_COLOR,
  fontSize: 14.0,
  fontWeight: FontWeight.w500,
);

const bottomSheetMenuTextStyle = TextStyle(
  height: 1.0,
  fontSize: 16.0,
  fontWeight: FontWeight.w400,
);
