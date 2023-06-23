import 'package:flutter/material.dart';

class TextInput extends StatelessWidget {
  final String hintText;
  final Color backgroundColor;
  const TextInput({
    required this.hintText,
    required this.backgroundColor,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextField(
      decoration: InputDecoration(
        hintText: hintText,
        filled: true,
        fillColor: backgroundColor,
        border: InputBorder.none,
        contentPadding: const EdgeInsets.all(18.0),
        focusedBorder: outlineInputBorderStyle(),
        enabledBorder: outlineInputBorderStyle(),
      ),
    );
  }

  OutlineInputBorder outlineInputBorderStyle() {
    return const OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.all(
        Radius.circular(10.0),
      ),
    );
  }
}
