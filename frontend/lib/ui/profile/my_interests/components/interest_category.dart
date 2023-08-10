import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
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
        color: isSelected ? primaryColor : commonWhiteColor,
        border: Border.all(
          color: isSelected ? primaryColor : circularBorderColor,
        ),
        borderRadius: BorderRadius.circular(50.0),
      ),
      child: Text(
        name,
        style: isSelected
            ? commonSubRegularStyle.copyWith(
                color: commonWhiteColor,
                fontWeight: FontWeight.w700,
              )
            : commonSubRegularStyle,
      ),
    );
  }
}
