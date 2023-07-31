import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';

class SettingScreen extends StatelessWidget {
  const SettingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: AppBar(
        backgroundColor: WHITE_COLOR,
        elevation: 0,
        title: const Text('설정'),
        titleTextStyle: appBarTitleStyle.copyWith(
          fontSize: 17.0,
        ),
        centerTitle: true,
        foregroundColor: BLACK_COLOR,
      ),
      child: Center(
        child: Text('설정'),
      ),
    );
  }
}
