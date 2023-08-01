import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/components/subpage_appbar.dart';

class SubPageLayout extends StatelessWidget {
  final Widget child;

  final String? appBarTitle;
  final Color? backgroundColor;
  final AppBar? appBar;
  final Widget? bottomNavigationBar;

  const SubPageLayout({
    required this.child,
    this.appBarTitle,
    this.backgroundColor,
    this.bottomNavigationBar,
    this.appBar,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: backgroundColor ?? WHITE_COLOR,
      appBar: renderAppBar(),
      body: child,
      bottomNavigationBar: bottomNavigationBar,
    );
  }

  AppBar? renderAppBar() {
    if (appBarTitle == null) {
      return null;
    } else {
      return subPageAppBar(
        appBarTitle: appBarTitle!,
      );
    }
  }
}
