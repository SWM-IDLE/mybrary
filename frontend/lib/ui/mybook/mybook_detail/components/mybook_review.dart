import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class MyBookReview extends StatelessWidget {
  final String content;
  final double starRating;
  final String createdAt;
  final double updatedAt;

  const MyBookReview({
    required this.content,
    required this.starRating,
    required this.createdAt,
    required this.updatedAt,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SliverPadding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      sliver: SliverToBoxAdapter(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 10.0),
            const Text(
              '마이 리뷰',
              style: commonSubBoldStyle,
            ),
            const SizedBox(height: 20.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    starRatingRow(),
                    const SizedBox(width: 10.0),
                    Text(
                      '$starRating',
                      style: commonSubBoldStyle.copyWith(
                        fontSize: 24.0,
                      ),
                    ),
                  ],
                ),
                Text(
                  '리뷰 0개',
                  style: bookDetailSubStyle.copyWith(
                    decoration: TextDecoration.underline,
                  ),
                )
              ],
            ),
            const SizedBox(height: 15.0),
            Text(
              content,
              style: bookDetailSubStyle,
            ),
            const SizedBox(height: 15.0),
            Text(
              createdAt,
              style: bookDetailSubStyle,
              textAlign: TextAlign.right,
            ),
            const SizedBox(height: 20.0),
          ],
        ),
      ),
    );
  }

  Row starRatingRow() {
    return Row(
      children: List.generate(5, (index) => index)
          .map(
            (e) => Image.asset(
              'assets/img/icon/star.png',
              // 별점 표시. 예로, 3.3은 3점이며 4.8은 4점으로 표시
              color: e < starRating.floor()
                  ? bookStarColor
                  : bookStarDisabledColor,
            ),
          )
          .toList(),
    );
  }
}
