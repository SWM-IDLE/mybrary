import 'package:flutter/material.dart';

class CustomSnackBar extends StatelessWidget {
  final String message;
  final Duration duration;
  final SnackBarAction? action;

  const CustomSnackBar({
    required this.message,
    required this.duration,
    this.action,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return ScaffoldMessenger(
      child: SnackBar(
        content: Text(message),
        duration: duration,
        action: action,
      ),
    );
  }
}
