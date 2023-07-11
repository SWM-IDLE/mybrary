import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';

class BookDescription extends StatelessWidget {
  final BookSearchData bookDetail;
  const BookDescription({
    required this.bookDetail,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '책 소개',
            style: TextStyle(
              fontSize: 16.0,
              fontWeight: FontWeight.w700,
            ),
          ),
          SizedBox(
            height: 10.0,
          ),
          Text(
            bookDetail.description!,
            style: TextStyle(
              color: BOOK_DETAIL_COLOR,
              fontSize: 14.0,
              fontWeight: FontWeight.w400,
            ),
          ),
        ],
      ),
    );
  }
}
