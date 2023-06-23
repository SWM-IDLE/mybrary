import 'package:flutter/material.dart';

class LoginButton extends StatelessWidget {
  final String btnText;
  final Color btnBackgroundColor;
  final Color textColor;
  const LoginButton({
    required this.btnText,
    required this.btnBackgroundColor,
    required this.textColor,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      onPressed: () {},
      style: ElevatedButton.styleFrom(
        textStyle: TextStyle(
          fontSize: 16.0,
        ),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
        backgroundColor: btnBackgroundColor,
        minimumSize: Size(100, 55),
      ),
      child: Text(
        btnText,
        style: TextStyle(
          color: textColor,
          fontWeight: FontWeight.w700,
        ),
      ),
    );
  }
}
