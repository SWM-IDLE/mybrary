import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class DefaultLayout extends StatelessWidget {
  final Widget child;
  final Widget? bottomNavigationBar;
  final String? title;
  final AppBar? appBar;

  const DefaultLayout({
    required this.child,
    this.bottomNavigationBar,
    this.title,
    this.appBar,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: WHITE_COLOR,
      appBar: appBar,
      body: child,
      bottomNavigationBar: bottomNavigationBar,
    );
  }
}
