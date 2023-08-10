import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class CircularLoading extends StatelessWidget {
  const CircularLoading({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: MediaQuery.of(context).size.height * 0.5,
      child: Center(
        child: CircularProgressIndicator(
          backgroundColor: primaryColor.withOpacity(0.2),
          valueColor: const AlwaysStoppedAnimation<Color>(primaryColor),
        ),
      ),
    );
  }
}
