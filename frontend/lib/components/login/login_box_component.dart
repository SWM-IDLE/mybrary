import 'package:flutter/material.dart';
import 'package:mybrary/constants/color.dart';

class LoginBox extends StatelessWidget {
  final Widget signWidget;
  const LoginBox({
    required this.signWidget,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 20.0),
      decoration: BoxDecoration(
        color: LOGIN_BACKGROUND_COLOR.withOpacity(0.5),
        borderRadius: const BorderRadius.all(
          Radius.circular(10.0),
        ),
      ),
      child: signWidget,
    );
  }
}
