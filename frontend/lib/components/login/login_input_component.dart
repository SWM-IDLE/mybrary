import 'package:flutter/material.dart';

class LoginInput extends StatelessWidget {
  final String hintText;
  final Color backgroundColor;

  const LoginInput({
    required this.hintText,
    required this.backgroundColor,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      decoration: InputDecoration(
        hintText: hintText,
        filled: true,
        fillColor: backgroundColor,
        border: InputBorder.none,
        contentPadding: const EdgeInsets.all(18.0),
        focusedBorder: loginInputBorderStyle(),
        enabledBorder: loginInputBorderStyle(),
      ),
    );
  }

  OutlineInputBorder loginInputBorderStyle() {
    return const OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.all(
        Radius.circular(10.0),
      ),
    );
  }
}
