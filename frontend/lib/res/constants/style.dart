import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

// common style
const appBarTitleStyle = TextStyle(
  color: BLACK_COLOR,
  fontSize: 22.0,
  fontWeight: FontWeight.w700,
);

const bottomSheetStyle = RoundedRectangleBorder(
  borderRadius: BorderRadius.only(
    topLeft: Radius.circular(20.0),
    topRight: Radius.circular(20.0),
  ),
);

const bottomSheetMenuTextStyle = TextStyle(
  color: GREY_06_COLOR,
  height: 1.0,
  fontSize: 16.0,
  fontWeight: FontWeight.w400,
);

const commonSubTitleStyle = TextStyle(
  fontSize: 17.0,
  fontWeight: FontWeight.w700,
);

const commonSubThinStyle = TextStyle(
  color: GREY_05_COLOR,
  fontSize: 12.0,
  fontWeight: FontWeight.w300,
);

const commonButtonTextStyle = TextStyle(
  fontSize: 15.0,
  fontWeight: FontWeight.w700,
);

const commonDialogMessageStyle = TextStyle(
  color: BLACK_COLOR,
  fontSize: 15.0,
  fontWeight: FontWeight.w400,
);

final commonMenuButtonStyle = TextButton.styleFrom(
  backgroundColor: Colors.transparent,
  foregroundColor: Colors.transparent,
  surfaceTintColor: Colors.transparent,
  splashFactory: NoSplash.splashFactory,
);

// profile page style
const followTextStyle = TextStyle(
  fontSize: 14.0,
  fontWeight: FontWeight.w400,
);

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

const profileEditTitleStyle = TextStyle(
  color: GREY_06_COLOR,
  fontSize: 14.0,
  fontWeight: FontWeight.w500,
);

const profileEditContentStyle = TextStyle(
  color: GREY_05_COLOR,
  fontSize: 14.0,
  fontWeight: FontWeight.w400,
);