import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/search_detail/components/book_summary.dart';

class BookDetailHeader extends StatelessWidget {
  final String thumbnail;
  final String title;
  final List<Authors> authors;

  const BookDetailHeader({
    required this.thumbnail,
    required this.title,
    required this.authors,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Container(
          width: 176,
          height: 254,
          decoration: ShapeDecoration(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
            shadows: [
              BoxShadow(
                color: BLACK_COLOR.withOpacity(0.3),
                blurRadius: 6,
                offset: const Offset(0, 4),
                spreadRadius: 0,
              )
            ],
            image: DecorationImage(
              image: NetworkImage(thumbnail),
              fit: BoxFit.fill,
            ),
          ),
        ),
        const SizedBox(height: 20.0),
        BookSummary(
          title: title,
          authors: authors,
        ),
      ],
    );
  }
}
