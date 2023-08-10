import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class IsbnScanHeader extends StatelessWidget {
  final double width;
  final double height;

  const IsbnScanHeader({
    required this.width,
    required this.height,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Positioned(
      top: 0,
      child: Container(
        width: width,
        height: height * 0.08,
        decoration: BoxDecoration(
          color: myBookScanBackgroundColor.withOpacity(0.7),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            Text(
              '바코드 스캔',
              style: TextStyle(
                color: primaryColor,
                fontSize: 15.0,
              ),
            ),
            GestureDetector(
              onTap: () {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text('준비 중인 기능입니다.'),
                    duration: Duration(seconds: 2),
                  ),
                );
              },
              child: Text(
                '책장 스캔',
                style: TextStyle(
                  color: commonWhiteColor.withOpacity(0.3),
                  fontSize: 15.0,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
