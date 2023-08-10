import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class BookDetailDivider extends StatelessWidget {
  const BookDetailDivider({super.key});

  @override
  Widget build(BuildContext context) {
    return const Padding(
      padding: EdgeInsets.symmetric(vertical: 12.0),
      child: Divider(
        height: 1,
        thickness: 6,
        color: greyF1F2F5,
      ),
    );
  }
}
