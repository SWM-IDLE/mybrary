import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class Logo extends StatelessWidget {
  final String logoText;
  const Logo({
    required this.logoText,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: 8.0,
        vertical: 72.0,
      ),
      child: Text(
        logoText,
        style: commonSubBoldStyle.copyWith(
          height: 1.6,
          fontSize: 28.0,
          color: commonWhiteColor,
        ),
      ),
    );
  }
}
