import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class MyBookRecord extends StatelessWidget {
  final MeaningTag meaningTag;
  final String readStatus;
  final bool showable;
  final bool shareable;
  final bool exchangeable;
  final String startDateOfPossession;

  const MyBookRecord({
    required this.meaningTag,
    required this.readStatus,
    required this.showable,
    required this.shareable,
    required this.exchangeable,
    required this.startDateOfPossession,
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
                  meaningTag.quote == '' ? '어떤 의미인가요?' : meaningTag.quote!,
              colorCode: meaningTag.colorCode == ''
                  ? grey777777
                  : Color(
                      int.parse(
                          '0xFF${meaningTag.colorCode!.replaceFirst('#', 'to')}'),
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
