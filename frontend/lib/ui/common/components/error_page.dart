import 'package:flutter/material.dart';

class ErrorPage extends StatelessWidget {
  final String errorMessage;

  const ErrorPage({
    required this.errorMessage,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Center(
        child: Text(
          errorMessage,
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}
