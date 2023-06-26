import 'package:flutter/material.dart';

class Logo extends StatelessWidget {
  final String logoText;
  const Logo({
    required this.logoText,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text(
        logoText,
        style: TextStyle(
          fontSize: 30.0,
          fontWeight: FontWeight.w700,
        ),
        textAlign: TextAlign.center,
      ),
    );
  }
}
