import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class BookSummary extends StatelessWidget {
  final String title;
  final List<Authors> authors;
  const BookSummary({
    required this.title,
    required this.authors,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 18.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(height: 2.0),
          Text(
            title,
            style: bookDetailTitleStyle,
          ),
          const SizedBox(height: 6.0),
          bookDescription(bookAuthorsOrTranslators(authors)),
          // Row(
          //   children: [
          //     Row(
          //       children: List.generate(5, (index) => index)
          //           .map((e) => Image.asset(
          //                 'assets/img/icon/star.png',
          //                 // 별점의 소수점을 버림 처리하여 별점을 표시
          //                 // 예를 들어, 3.3은 3점이며 4.8은 4점으로 표시
          //                 color: e <
          //                         bookSearchDetailResponseData.starRating!
          //                             .floor()
          //                     ? BOOK_STAR_COLOR
          //                     : BOOK_STAR_DISABLED_COLOR,
          //               ))
          //           .toList(),
          //     ),
          //     SizedBox(
          //       width: 10.0,
          //     ),
          //     Text(
          //       bookSearchDetailResponseData.starRating.toString(),
          //       style: TextStyle(
          //         color: BOOK_STAR_RATING_COLOR,
          //         fontSize: 20.0,
          //         fontWeight: FontWeight.w700,
          //       ),
          //     ),
          //   ],
          // ),
        ],
      ),
    );
  }

  Widget bookDescription(String description) {
    return Text(
      description,
      style: commonSubRegularStyle.copyWith(
        color: BOOK_DESCRIPTION_COLOR,
      ),
    );
  }
}
