import 'package:flutter/material.dart';
import 'package:mybrary/constants/color.dart';

class LoginInput extends StatelessWidget {
  final String hintText;
  final String initialValue;
  final String? suffixText;
  final bool? isEnabled;
  final FormFieldSetter<String> onSaved;
  final FormFieldValidator<String> validator;

  const LoginInput({
    required this.hintText,
    required this.validator,
    required this.initialValue,
    required this.onSaved,
    this.suffixText,
    this.isEnabled,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      enabled: isEnabled,
      onSaved: onSaved,
      initialValue: initialValue,
      validator: validator,
      cursorColor: ORANGE_COLOR,
      decoration: InputDecoration(
        suffixIcon: Padding(
          padding: EdgeInsets.all(15.0),
          child: suffixText != null ? Text('$suffixText') : null,
        ),
        hintText: hintText,
        contentPadding: const EdgeInsets.symmetric(vertical: 16.0),
        focusedBorder: loginInputBorderStyle(),
      ),
    );
  }

  UnderlineInputBorder loginInputBorderStyle() {
    return const UnderlineInputBorder(
      borderSide: BorderSide(
        color: BLACK_COLOR,
      ),
    );
  }
}
