import 'package:flutter/material.dart';
import 'package:html/parser.dart';
import 'package:mybrary/res/constants/style.dart';

class BookContents extends StatelessWidget {
  final String toc;
  const BookContents({
    required this.toc,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          toc == '' ? '목차가 없습니다.' : parse(toc).documentElement!.text,
          style: bookDetailDescriptionStyle,
        ),
        const SizedBox(height: 10.0),
      ],
    );
  }
}
