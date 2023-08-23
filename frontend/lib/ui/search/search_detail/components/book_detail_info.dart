import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class BookDetailInfo extends StatelessWidget {
  final String isbn13;
  final String publicationDate;
  final String category;
  final int pages;
  final String publisher;
  final double starRating;
  final int reviewCount;
  final String link;
  final double aladinStarRating;
  final GestureTapCallback? onTapReview;

  const BookDetailInfo({
    required this.isbn13,
    required this.publicationDate,
    required this.category,
    required this.pages,
    required this.publisher,
    required this.starRating,
    required this.reviewCount,
    required this.link,
    required this.aladinStarRating,
    this.onTapReview,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    String category = this.category.split('>').join(' > ');
    List<String> publicationDate = this.publicationDate.split('-');

    final [year, month, day] = publicationDate;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 10.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Row(
                children: [
                  Text(
                    '$starRating',
                    style: commonSubBoldStyle.copyWith(
                      fontSize: 24.0,
                    ),
                  ),
                  const SizedBox(width: 10.0),
                  starRatingRow(starRating: starRating),
                ],
              ),
              InkWell(
                onTap: () => onTapReview!(),
                child: Text(
                  '리뷰 $reviewCount개',
                  style: bookDetailSubStyle.copyWith(
                    decoration: TextDecoration.underline,
                  ),
                ),
              )
            ],
          ),
          const SizedBox(height: 20.0),
          infoItem('발행일', '$year년 $month월 $day일'),
          const SizedBox(height: 15.0),
          infoMultipleItem(
            '카테고리',
            Row(
              children: [
                Text(
                  category == ''
                      ? '제공되는 카테고리가 없습니다.'
                      : substringCategory(category),
                  style: bookDetailInfoStyle,
                ),
                const SizedBox(width: 4.0),
                Tooltip(
                  message: category,
                  textStyle: bookDetailInfoStyle.copyWith(
                    fontSize: 12.0,
                    color: grey262626,
                  ),
                  triggerMode: TooltipTriggerMode.tap,
                  decoration: const BoxDecoration(
                    color: greyF1F2F5,
                    borderRadius: BorderRadius.all(Radius.circular(4.0)),
                  ),
                  verticalOffset: 16.0,
                  showDuration: const Duration(seconds: 3),
                  child: const Icon(
                    Icons.info,
                    size: 18.0,
                    color: greyDDDDDD,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 15.0),
          infoItem('쪽수', '$pages쪽'),
          const SizedBox(height: 15.0),
          infoItem('출판사', publisher),
          const SizedBox(height: 15.0),
          infoMultipleItem(
            '알라딘 평점',
            Row(
              children: [
                SvgPicture.asset(
                  'assets/svg/icon/small/star.svg',
                  height: 18.0,
                ),
                const SizedBox(width: 4.0),
                Text(
                  '$aladinStarRating / 5.0',
                  style: bookDetailInfoStyle,
                ),
              ],
            ),
          ),
          const SizedBox(height: 20.0),
        ],
      ),
    );
  }

  Row infoItem(String itemTitle, String itemDescription) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          itemTitle,
          style: bookDetailSubStyle,
        ),
        Text(
          itemDescription,
          style: bookDetailInfoStyle,
        ),
      ],
    );
  }

  Row infoMultipleItem(String itemTitle, Widget itemDescription) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          itemTitle,
          style: bookDetailSubStyle,
        ),
        itemDescription,
      ],
    );
  }

  String substringCategory(String category) {
    return category.length > 16 ? '${category.substring(0, 16)}..' : category;
  }
}
