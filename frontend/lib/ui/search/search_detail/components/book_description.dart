import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';

class BookDescription extends StatelessWidget {
  final BookSearchData bookSearchData;
  const BookDescription({
    required this.bookSearchData,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: MediaQuery.of(context).size.width,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              '책소개',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w700,
              ),
            ),
            SizedBox(
              height: 16.0,
            ),
            Text(
              bookSearchData.description! == ''
                  ? '책 소개가 없습니다.'
                  : bookSearchData.description!,
              style: TextStyle(
                height: 1.4,
                color: BOOK_DETAIL_COLOR,
                fontSize: 14.0,
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
