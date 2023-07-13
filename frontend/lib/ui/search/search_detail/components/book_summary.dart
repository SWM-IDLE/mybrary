import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';

class BookSummary extends StatelessWidget {
  final BookSearchData bookDetail;
  const BookSummary({
    required this.bookDetail,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final DateTime publishDate = DateTime.parse(bookDetail.publicationDate!);
    return SizedBox(
      width: MediaQuery.of(context).size.width,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              bookDetail.title!,
              style: TextStyle(
                fontSize: 18.0,
                fontWeight: FontWeight.w700,
              ),
            ),
            SizedBox(
              height: 10.0,
            ),
            Row(
              children: [
                bookDescription('${bookDetail.authors!.map(
                      (e) => e,
                    ).join(', ')} 저'),
                SizedBox(
                  width: 4.0,
                ),
                bookDescription(' | '),
                SizedBox(
                  width: 4.0,
                ),
                bookDescription(bookDetail.publisher!),
                SizedBox(
                  width: 10.0,
                ),
                bookDescription('${publishDate.year}.${publishDate.month}'),
              ],
            ),
            SizedBox(
              height: 10.0,
            ),
            Row(
              children: [
                Row(
                  children: List.generate(5, (index) => index)
                      .map((e) => Image.asset(
                            'assets/icon/star_icon.png',
                            // 별점의 소수점을 버림 처리하여 별점을 표시
                            // 예를 들어, 3.3은 3점이며 4.8은 4점으로 표시
                            color: e < bookDetail.starRating!.floor()
                                ? BOOK_STAR_COLOR
                                : BOOK_STAR_DISABLED_COLOR,
                          ))
                      .toList(),
                ),
                SizedBox(
                  width: 10.0,
                ),
                Text(
                  bookDetail.starRating.toString(),
                  style: TextStyle(
                    color: BOOK_STAR_RATING_COLOR,
                    fontSize: 20.0,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget bookDescription(String description) {
    return Text(
      description,
      style: TextStyle(
        color: BOOK_DESCRIPTION_COLOR,
        fontSize: 14.0,
        fontWeight: FontWeight.w400,
      ),
    );
  }
}
