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

const commonSubRegularStyle = TextStyle(
  color: GREY_06_COLOR,
  fontSize: 14.0,
  fontWeight: FontWeight.w400,
);

const commonSubMediumStyle = TextStyle(
  color: GREY_06_COLOR,
  fontSize: 16.0,
  fontWeight: FontWeight.w500,
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

const boxBorderRadiusStyle = RoundedRectangleBorder(
  borderRadius: BorderRadius.all(
    Radius.circular(50.0),
  ),
);

const saveTextButtonStyle = TextStyle(
  color: PRIMARY_COLOR,
  fontSize: 17.0,
  fontWeight: FontWeight.w400,
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
    color: circularBorderColor,
    width: 1,
  ),
);

const introInputBorderStyle = OutlineInputBorder(
  borderSide: BorderSide(
    color: circularBorderColor,
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

const interestDescriptionStyle = TextStyle(
  color: GREY_06_COLOR,
  fontSize: 16.0,
  fontWeight: FontWeight.w400,
);

const followNicknameStyle = TextStyle(
  color: BLACK_COLOR,
  fontSize: 15.0,
  fontWeight: FontWeight.w400,
);

const followButtonTextStyle = TextStyle(
  color: BLACK_COLOR,
  fontSize: 12.0,
  fontWeight: FontWeight.w400,
);

// button style
final disableAnimationButtonStyle = ButtonStyle(
  backgroundColor: MaterialStateProperty.all(Colors.transparent),
  elevation: MaterialStateProperty.all(0.0),
  splashFactory: NoSplash.splashFactory,
  overlayColor: MaterialStateProperty.all(Colors.transparent),
);

final followButtonRoundStyle = RoundedRectangleBorder(
  borderRadius: BorderRadius.circular(10.0),
);

const popularKeywordTextStyle = TextStyle(
  color: circularTextColor,
  fontSize: 14.0,
  fontWeight: FontWeight.w400,
);

// border style
final searchInputBorderStyle = OutlineInputBorder(
  borderSide: BorderSide.none,
  borderRadius: BorderRadius.circular(5.0),
);
