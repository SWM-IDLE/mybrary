import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class IsbnScanAppBar extends StatelessWidget implements PreferredSizeWidget {
  final AppBar appBar;

  const IsbnScanAppBar({
    required this.appBar,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AppBar(
      elevation: 0.0,
      backgroundColor: myBookScanBackgroundColor,
      automaticallyImplyLeading: false,
      centerTitle: true,
      title: Text(
        '마이북 스캔',
        style: TextStyle(
          color: commonWhiteColor,
          fontSize: 16.0,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }

  @override
  // TODO: implement preferredSize
  Size get preferredSize => Size.fromHeight(appBar.preferredSize.height);
}
