import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchLoading extends StatelessWidget {
  const SearchLoading({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: CircularProgressIndicator(
        backgroundColor: PRIMARY_COLOR.withOpacity(0.2),
        valueColor: const AlwaysStoppedAnimation<Color>(PRIMARY_COLOR),
      ),
    );
  }
}
