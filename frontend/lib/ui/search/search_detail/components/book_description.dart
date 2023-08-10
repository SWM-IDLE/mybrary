import 'package:flutter/material.dart';
import 'package:html/parser.dart';
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
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          '부제',
          style: commonSubMediumStyle,
        ),
        const SizedBox(height: 10.0),
        Text(
          subTitle == '' ? '부제가 없습니다.' : parse(subTitle).documentElement!.text,
          style: bookDetailDescriptionStyle,
        ),
        const SizedBox(height: 24.0),
        const Text(
          '책 설명',
          style: commonSubMediumStyle,
        ),
        const SizedBox(height: 10.0),
        Text(
          description == ''
              ? '책 설명이 없습니다.'
              : parse(description).documentElement!.text,
          style: bookDetailDescriptionStyle,
        ),
        const SizedBox(height: 10.0),
      ],
    );
  }
}
