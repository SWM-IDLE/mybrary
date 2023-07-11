import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class BookDetailAppbar extends AppBar {
  BookDetailAppbar({super.key});

  Widget build(BuildContext context) {
    return AppBar(
      elevation: 0,
      backgroundColor: BOOK_BACKGROUND_COLOR,
      iconTheme: IconThemeData(color: Colors.black),
      actions: [
        IconButton(
          onPressed: () {
            Navigator.of(context)
                .pushNamedAndRemoveUntil('/home', (route) => false);
          },
          icon: Image.asset(
            'assets/icon/home_icon.png',
          ),
        ),
      ],
    );
  }
}
