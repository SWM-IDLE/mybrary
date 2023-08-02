import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class CircularLoading extends StatelessWidget {
  const CircularLoading({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: MediaQuery.of(context).size.height * 0.5,
      child: Center(
        child: CircularProgressIndicator(
          backgroundColor: PRIMARY_COLOR.withOpacity(0.2),
          valueColor: const AlwaysStoppedAnimation<Color>(PRIMARY_COLOR),
        ),
      ),
    );
  }
}
