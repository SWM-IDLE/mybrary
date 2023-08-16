import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class MyBookDetailRecord extends StatelessWidget {
  final String readStatus;
  final bool showable;
  final bool shareable;
  final bool exchangeable;
  final String startDateOfPossession;
  final String meaningTagColorCode;
  final String meaningTagQuote;

  const MyBookDetailRecord({
    required this.readStatus,
    required this.showable,
    required this.shareable,
    required this.exchangeable,
    required this.startDateOfPossession,
    required this.meaningTagColorCode,
    required this.meaningTagQuote,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    String readStatusText = '읽기전';
    String shareOrExchange = '고민중이에요.';

    if (shareable && exchangeable) {
      shareOrExchange = '교환/나눔';
    } else if (shareable) {
      shareOrExchange = '나눔';
    } else if (exchangeable) {
      shareOrExchange = '교환';
    }

    if (readStatus == 'READING') {
      readStatusText = '읽는중';
    } else if (readStatus == 'COMPLETED') {
      readStatusText = '완독함';
    }

    return SliverPadding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      sliver: SliverToBoxAdapter(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 10.0),
            const Text(
              '마이북 기록',
              style: commonSubBoldStyle,
            ),
            const SizedBox(height: 20.0),
            detailItem(
              itemTitle: '나에게 이 책은',
              itemDescription:
                  meaningTagQuote == '' ? '어떤 의미인가요?' : meaningTagQuote,
              colorCode: meaningTagColorCode == ''
                  ? commonBlackColor
                  : Color(
                      int.parse('0xFF$meaningTagColorCode'),
                    ),
            ),
            const SizedBox(height: 15.0),
            detailItem(
              itemTitle: '독서 상태',
              itemDescription: readStatusText,
            ),
            const SizedBox(height: 15.0),
            detailItem(
              itemTitle: '교환/나눔',
              itemDescription: shareOrExchange,
            ),
            const SizedBox(height: 15.0),
            detailItem(
              itemTitle: '공개 여부',
              itemDescription: showable ? '공개' : '비공개',
            ),
            const SizedBox(height: 15.0),
            detailItem(
              itemTitle: '소장일',
              itemDescription: startDateOfPossession,
            ),
            const SizedBox(height: 20.0),
          ],
        ),
      ),
    );
  }

  Row detailItem({
    required String itemTitle,
    required String itemDescription,
    Color? colorCode,
  }) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          itemTitle,
          style: bookDetailSubStyle,
        ),
        Text(
          itemDescription,
          style: bookDetailInfoStyle.copyWith(color: colorCode),
        ),
      ],
    );
  }
}
