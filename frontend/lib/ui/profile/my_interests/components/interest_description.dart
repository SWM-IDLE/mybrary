import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/style.dart';

class InterestDescription extends StatelessWidget {
  final int startIndex;
  final int endIndex;
  final String description;

  const InterestDescription({
    required this.description,
    required this.startIndex,
    required this.endIndex,
    super.key,
  });

  String descriptionSubstring(int startIndex, int endIndex) =>
      description.substring(startIndex, endIndex);

  @override
  Widget build(BuildContext context) {
    return startIndex > 0
        ? Row(
            children: [
              Text(
                descriptionSubstring(0, startIndex),
                style: interestDescriptionStyle,
              ),
              Text(
                descriptionSubstring(startIndex, endIndex),
                style: interestDescriptionStyle.copyWith(
                  fontWeight: FontWeight.w700,
                ),
              ),
              Text(
                descriptionSubstring(endIndex, description.length),
                style: interestDescriptionStyle,
              ),
            ],
          )
        : Row(
            children: [
              Text(
                descriptionSubstring(0, endIndex),
                style: interestDescriptionStyle.copyWith(
                  fontWeight: FontWeight.w700,
                ),
              ),
              Text(
                descriptionSubstring(endIndex, description.length),
                style: interestDescriptionStyle,
              ),
            ],
          );
  }
}
