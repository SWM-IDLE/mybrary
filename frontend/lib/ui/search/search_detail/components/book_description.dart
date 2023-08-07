import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/style.dart';

class BookDescription extends StatelessWidget {
  final String subTitle;
  final String description;
  const BookDescription({
    required this.subTitle,
    required this.description,
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
            '부제',
            style: commonSubBoldStyle,
          ),
          const SizedBox(height: 10.0),
          Text(
            subTitle == '' ? '부제가 없습니다.' : subTitle,
            style: bookDetailDescriptionStyle,
          ),
          const SizedBox(height: 24.0),
          const Text(
            '책 소개',
            style: commonSubBoldStyle,
          ),
          const SizedBox(height: 10.0),
          Text(
            description == '' ? '책 소개가 없습니다.' : description,
            style: bookDetailDescriptionStyle,
          ),
          const SizedBox(height: 10.0),
        ],
      ),
    );
  }
}
