import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/style.dart';

class BookDetails extends StatelessWidget {
  final String isbn10;
  final String isbn13;
  final int weight;
  final int sizeDepth;
  final int sizeHeight;
  final int sizeWidth;

  const BookDetails({
    required this.isbn10,
    required this.isbn13,
    required this.weight,
    required this.sizeDepth,
    required this.sizeHeight,
    required this.sizeWidth,
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
            '세부 정보',
            style: commonSubBoldStyle,
          ),
          const SizedBox(height: 10.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                'ISBN 10',
                style: bookDetailDescriptionStyle,
              ),
              Text(
                isbn10,
                style: bookDetailDescriptionStyle,
              ),
            ],
          ),
          const SizedBox(height: 8.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                'ISBN 13',
                style: bookDetailDescriptionStyle,
              ),
              Text(
                isbn13,
                style: bookDetailDescriptionStyle,
              ),
            ],
          ),
          const SizedBox(height: 8.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                '무게',
                style: bookDetailDescriptionStyle,
              ),
              Text(
                '${weight}g',
                style: bookDetailDescriptionStyle,
              ),
            ],
          ),
          const SizedBox(height: 8.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                '크기',
                style: bookDetailDescriptionStyle,
              ),
              Text(
                '${sizeWidth}x${sizeHeight}x${sizeDepth}mm',
                style: bookDetailDescriptionStyle,
              ),
            ],
          ),
          const SizedBox(height: 10.0),
        ],
      ),
    );
  }
}
