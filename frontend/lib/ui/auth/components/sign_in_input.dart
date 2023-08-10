import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class SignInInput extends StatelessWidget {
  final String hintText;
  final String initialValue;
  final bool? isEnabled;
  final bool obscureText;
  final FormFieldSetter<String> onSaved;
  final FormFieldValidator<String> validator;

  const SignInInput({
    required this.hintText,
    required this.validator,
    required this.initialValue,
    required this.onSaved,
    this.isEnabled,
    this.obscureText = false,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      enabled: isEnabled,
      onSaved: onSaved,
      obscureText: obscureText,
      initialValue: initialValue,
      validator: validator,
      cursorColor: commonOrangeColor,
      decoration: InputDecoration(
        hintText: hintText,
        contentPadding: const EdgeInsets.symmetric(vertical: 16.0),
        focusedBorder: loginInputBorderStyle(),
      ),
    );
  }

  UnderlineInputBorder loginInputBorderStyle() {
    return const UnderlineInputBorder(
      borderSide: BorderSide(
        color: commonBlackColor,
      ),
    );
  }
}
