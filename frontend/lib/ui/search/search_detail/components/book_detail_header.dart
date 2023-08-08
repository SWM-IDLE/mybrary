import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

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
        Padding(
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
              Text(
                bookAuthorsOrTranslators(authors),
                style: commonSubRegularStyle.copyWith(
                  color: BOOK_DESCRIPTION_COLOR,
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
