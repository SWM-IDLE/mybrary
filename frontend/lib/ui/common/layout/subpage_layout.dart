import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class SubPageLayout extends StatelessWidget {
  final Widget child;

  final String? appBarTitle;
  final Color? backgroundColor;
  final AppBar? appBar;
  final Widget? bottomNavigationBar;
  final List<Widget>? appBarActions;
  final bool? resizeToAvoidBottomInset;

  const SubPageLayout({
    required this.child,
    this.appBarTitle,
    this.backgroundColor,
    this.bottomNavigationBar,
    this.appBar,
    this.appBarActions,
    this.resizeToAvoidBottomInset,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: resizeToAvoidBottomInset ?? true,
      backgroundColor: backgroundColor ?? commonWhiteColor,
      appBar: renderAppBar(),
      body: child,
      bottomNavigationBar: bottomNavigationBar,
    );
  }

  AppBar? renderAppBar() {
    if (appBarTitle == null) {
      return null;
    } else {
      return AppBar(
        elevation: 0,
        title: Text(appBarTitle!),
        titleTextStyle: appBarTitleStyle.copyWith(
          fontSize: 16.0,
        ),
        centerTitle: true,
        backgroundColor: commonWhiteColor,
        foregroundColor: commonBlackColor,
        actions: appBarActions,
      );
    }
  }
}
