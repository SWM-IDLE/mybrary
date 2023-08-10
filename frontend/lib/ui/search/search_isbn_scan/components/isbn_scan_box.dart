import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';

class IsbnScanBox extends StatelessWidget {
  final double width;
  final double height;

  const IsbnScanBox({
    required this.width,
    required this.height,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Positioned(
      top: height * 0.08,
      child: Column(
        children: [
          Container(
            width: width,
            height: height * 0.11,
            decoration: BoxDecoration(
              color: myBookScanBackgroundColor.withOpacity(0.3),
            ),
          ),
          Row(
            children: [
              Container(
                width: width * 0.1,
                height: height * 0.25,
                decoration: BoxDecoration(
                  color: myBookScanBackgroundColor.withOpacity(0.3),
                ),
              ),
              Stack(
                children: [
                  Container(
                    width: width * 0.8,
                    height: height * 0.25,
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: Colors.transparent,
                      ),
                    ),
                  ),
                  Positioned(
                    top: 0,
                    left: 0,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Container(
                          width: 30.0,
                          height: 3.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                        Container(
                          width: 3.0,
                          height: 30.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                      ],
                    ),
                  ),
                  Positioned(
                    top: 0,
                    right: 0,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Container(
                          width: 30.0,
                          height: 3.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                        Container(
                          width: 3.0,
                          height: 30.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                      ],
                    ),
                  ),
                  Positioned(
                    bottom: 0,
                    left: 0,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Container(
                          width: 3.0,
                          height: 30.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                        Container(
                          width: 30.0,
                          height: 3.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                      ],
                    ),
                  ),
                  Positioned(
                    bottom: 0,
                    right: 0,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Container(
                          width: 3.0,
                          height: 30.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                        Container(
                          width: 30.0,
                          height: 3.0,
                          decoration: BoxDecoration(
                            color: primaryColor,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              Container(
                width: width * 0.1,
                height: height * 0.25,
                decoration: BoxDecoration(
                  color: myBookScanBackgroundColor.withOpacity(0.3),
                ),
              ),
            ],
          ),
          Container(
            width: width,
            height: height * 0.11,
            decoration: BoxDecoration(
              color: myBookScanBackgroundColor.withOpacity(0.3),
            ),
          ),
        ],
      ),
    );
  }
}
