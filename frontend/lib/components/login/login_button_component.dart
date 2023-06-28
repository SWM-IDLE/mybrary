import 'package:flutter/material.dart';

class LoginButton extends StatelessWidget {
  final String btnText;
  final Color btnBackgroundColor;
  final Color textColor;
  final VoidCallback onPressed;
  final bool isOAuth;
  final Widget? btnIcon;
  final bool isEnabled;

  const LoginButton({
    required this.btnText,
    required this.btnBackgroundColor,
    required this.textColor,
    required this.onPressed,
    this.btnIcon,
    required this.isOAuth,
    required this.isEnabled,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final textContent = Text(
      btnText,
      style: TextStyle(
        color: textColor,
        fontWeight: FontWeight.w700,
      ),
    );

    final elevatedButtonStyle = ElevatedButton.styleFrom(
      textStyle: const TextStyle(
        fontSize: 16.0,
      ),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
      ),
      backgroundColor: btnBackgroundColor,
      minimumSize: const Size(100, 55),
      elevation: 0.0,
    );

    return isOAuth
        ? ElevatedButton.icon(
            icon: isOAuth ? btnIcon! : Icon(null),
            label: textContent,
            onPressed: isEnabled ? onPressed : null,
            style: elevatedButtonStyle,
          )
        : ElevatedButton(
            onPressed: isEnabled ? onPressed : null,
            style: elevatedButtonStyle,
            child: textContent,
          );
  }
}
