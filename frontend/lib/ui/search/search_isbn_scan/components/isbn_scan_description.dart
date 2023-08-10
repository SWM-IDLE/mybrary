import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class IsbnScanDescription extends StatelessWidget {
  final double width;
  final double height;

  const IsbnScanDescription({
    required this.width,
    required this.height,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    const barcodeDescriptionTextStyle = TextStyle(
      color: commonWhiteColor,
      fontSize: 15.0,
    );

    return Positioned(
      bottom: 0,
      child: Container(
        width: width,
        height: height * 0.35,
        decoration: BoxDecoration(
          color: myBookScanBackgroundColor.withOpacity(0.7),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Column(
              children: [
                Text(
                  '도서 뒷면의 바코드를 인식시켜',
                  style: barcodeDescriptionTextStyle,
                ),
                SizedBox(
                  height: 10.0,
                ),
                Text(
                  '책을 검색할 수 있어요',
                  style: barcodeDescriptionTextStyle,
                ),
              ],
            ),
            SizedBox(
              height: 40.0,
            ),
            ElevatedButton(
              onPressed: () => Navigator.of(context).pop(),
              style: ElevatedButton.styleFrom(
                fixedSize: Size(66.0, 66.0),
                backgroundColor: primaryColor,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(100.0),
                ),
              ),
              child: Icon(
                CupertinoIcons.xmark,
                color: commonWhiteColor,
                size: 44.0,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
