import 'package:flutter/material.dart';

class DividerRow extends StatelessWidget {
  const DividerRow({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 110,
      child: Divider(
        color: Color(0xFF313333),
        thickness: 1.0,
      ),
    );
  }
}
