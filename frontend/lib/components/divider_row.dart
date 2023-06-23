import 'package:flutter/material.dart';
import 'package:mybrary/constants/color.dart';

class DividerRow extends StatelessWidget {
  const DividerRow({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const SizedBox(
      width: 130,
      child: Divider(
        color: LESS_BLACK_COLOR,
        thickness: 1.0,
      ),
    );
  }
}
