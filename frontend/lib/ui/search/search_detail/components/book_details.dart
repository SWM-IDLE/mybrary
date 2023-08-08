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
          const SizedBox(height: 10.0),
          const Text(
            '세부 정보',
            style: commonSubBoldStyle,
          ),
          const SizedBox(height: 10.0),
          detailItem('ISBN 10', isbn10),
          const SizedBox(height: 8.0),
          detailItem('ISBN 13', isbn13),
          const SizedBox(height: 8.0),
          detailItem('무게', '${weight}g'),
          const SizedBox(height: 8.0),
          detailItem('크기', '${sizeWidth}x${sizeHeight}x${sizeDepth}mm'),
          const SizedBox(height: 20.0),
        ],
      ),
    );
  }

  Row detailItem(String itemTitle, String itemDescription) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          itemTitle,
          style: bookDetailDescriptionStyle,
        ),
        Text(
          itemDescription,
          style: bookDetailDescriptionStyle,
        ),
      ],
    );
  }
}
