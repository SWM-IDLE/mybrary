import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class BookDetailAppBar extends StatelessWidget implements PreferredSizeWidget {
  final AppBar appBar;
  const BookDetailAppBar({
    required this.appBar,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AppBar(
      elevation: 0.0,
      backgroundColor: BOOK_BACKGROUND_COLOR,
      iconTheme: IconThemeData(color: Colors.black),
      actions: [
        IconButton(
          onPressed: () {
            Navigator.of(context)
                .pushNamedAndRemoveUntil('/home', (route) => false);
          },
          icon: Image.asset(
            'assets/img/icon/home.png',
          ),
        ),
      ],
    );
  }

  @override
// TODO: implement preferredSize
  Size get preferredSize => Size.fromHeight(appBar.preferredSize.height);
}
