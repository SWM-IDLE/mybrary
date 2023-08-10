import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:url_launcher/url_launcher.dart';

class BookDetailProvider extends StatelessWidget {
  final String link;

  const BookDetailProvider({
    required this.link,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '도서 정보 제공',
            style: commonSubMediumStyle.copyWith(fontSize: 13.0),
          ),
          const SizedBox(height: 10.0),
          Container(
            height: 40.0,
            padding: const EdgeInsets.symmetric(horizontal: 6.0),
            decoration: const BoxDecoration(
              color: greyF1F2F5,
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Image.asset(
                  'assets/img/logo/aladin.png',
                  width: 92.0,
                  height: 22.0,
                ),
                InkWell(
                  onTap: () async {
                    await launchUrl(
                      Uri.parse(link),
                    );
                  },
                  child: const Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text('자세히보기', style: aladinTextStyle),
                      SizedBox(width: 2.0),
                      Icon(
                        Icons.keyboard_arrow_right_outlined,
                        size: 16.0,
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
