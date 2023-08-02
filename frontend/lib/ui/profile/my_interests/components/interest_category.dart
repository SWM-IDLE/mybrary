import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';

class InterestCategory extends StatelessWidget {
  final bool isSelected;
  final String name;

  const InterestCategory({
    required this.isSelected,
    required this.name,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: 16.0,
        vertical: 8.0,
      ),
      decoration: BoxDecoration(
        border: Border.all(
          color: isSelected ? primaryColor : circularBorderColor,
        ),
        borderRadius: BorderRadius.circular(50.0),
      ),
      child: Text(
        name,
        style: isSelected
            ? commonSubRegularStyle.copyWith(
                fontWeight: FontWeight.w700,
              )
            : commonSubRegularStyle,
      ),
    );
  }
}
