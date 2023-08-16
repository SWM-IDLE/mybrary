import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class HomeBookCount extends StatelessWidget {
  final int todayRegisteredBookCount;

  const HomeBookCount({
    required this.todayRegisteredBookCount,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          _divider(),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 8.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  '오늘 마이브러리에 등록된 책!',
                  style: todayRegisteredBookTextStyle,
                ),
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8.0,
                        vertical: 2.0,
                      ),
                      decoration: ShapeDecoration(
                        gradient: const LinearGradient(
                          begin: Alignment(0.00, -1.00),
                          end: Alignment(0, 1),
                          colors: [
                            homeTodayRegisteredBookColorTop,
                            homeTodayRegisteredBookColorCenter,
                            homeTodayRegisteredBookColorBottom,
                          ],
                        ),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(30.0),
                        ),
                      ),
                      child: Text(
                        todayRegisteredBookCount.toString().padLeft(6, '0'),
                        style: todayRegisteredBookTextStyle.copyWith(
                          color: commonWhiteColor,
                          letterSpacing: 2.0,
                        ),
                      ),
                    ),
                    const SizedBox(width: 4.0),
                    const Text(
                      '권',
                      style: todayRegisteredBookTextStyle,
                    ),
                  ],
                ),
              ],
            ),
          ),
          _divider(),
        ],
      ),
    );
  }

  Divider _divider() {
    return const Divider(
      color: greyDDDDDD,
      thickness: 1.0,
      height: 1.0,
    );
  }
}
