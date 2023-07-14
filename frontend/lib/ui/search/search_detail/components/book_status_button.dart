import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class BookStatusButton extends StatelessWidget {
  final List<Widget> children;

  const BookStatusButton({
    required this.children,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 20.0),
      decoration: BoxDecoration(
        border: Border.symmetric(
          horizontal: BorderSide(
            color: BOOK_DESCRIPTION_COLOR,
            width: 0.5,
          ),
        ),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: children,
      ),
    );
  }
}
