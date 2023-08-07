import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/style.dart';

class BookContents extends StatelessWidget {
  final String toc;
  const BookContents({
    required this.toc,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '목차',
            style: commonSubBoldStyle,
          ),
          const SizedBox(height: 10.0),
          Text(
            toc == '' ? '목차가 없습니다.' : toc,
            style: bookDetailDescriptionStyle,
          ),
          const SizedBox(height: 10.0),
        ],
      ),
    );
  }
}
