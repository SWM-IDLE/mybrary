import 'dart:io';

import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class BottomButton extends StatelessWidget {
  final bool isScrollingDown;
  final String buttonText;
  final GestureTapCallback? onTap;

  BottomButton({
    required this.isScrollingDown,
    required this.buttonText,
    this.onTap,
    super.key,
  });

  final double _bottomInset = Platform.isAndroid ? 0 : 12;

  @override
  Widget build(BuildContext context) {
    return AnimatedPositioned(
      bottom: isScrollingDown ? _bottomInset : -150,
      left: 0,
      right: 0,
      duration: const Duration(milliseconds: 500),
      curve: Curves.easeInOut,
      child: InkWell(
        onTap: onTap,
        child: Container(
          height: 76.0,
          padding: const EdgeInsets.symmetric(
            horizontal: 32.0,
            vertical: 12.0,
          ),
          child: Container(
            alignment: Alignment.center,
            decoration: BoxDecoration(
              color: primaryColor,
              borderRadius: BorderRadius.circular(30.0),
            ),
            child: Text(
              buttonText,
              style: bottomButtonTextStyle,
            ),
          ),
        ),
      ),
    );
  }
}
