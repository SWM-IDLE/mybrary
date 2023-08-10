import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:html/parser.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class BookDetailHeader extends StatelessWidget {
  final String thumbnail;
  final String title;
  final List<Authors> authors;
  final int interestCount;
  final int newInterestCount;
  final int readCount;
  final int holderCount;
  final bool interested;
  final bool isOnTapHeart;
  final GestureTapCallback? onTapInterestBook;

  const BookDetailHeader({
    required this.thumbnail,
    required this.title,
    required this.authors,
    required this.interestCount,
    required this.newInterestCount,
    required this.readCount,
    required this.holderCount,
    required this.interested,
    required this.isOnTapHeart,
    required this.onTapInterestBook,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    String heartUrl = isOnTapHeart ? 'heart_green.svg' : 'heart.svg';

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
                color: commonBlackColor.withOpacity(0.3),
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
                parse(title).documentElement!.text,
                style: bookDetailTitleStyle,
              ),
              const SizedBox(height: 6.0),
              Text(
                bookAuthorsOrTranslators(authors),
                style: commonSubRegularStyle.copyWith(
                  color: bookDescriptionColor,
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 24.0),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            bookStatusColumn(
              padding: 32.0,
              children: [
                InkWell(
                  onTap: () => onTapInterestBook!(),
                  child: Column(
                    children: [
                      SvgPicture.asset(
                        'assets/svg/icon/small/$heartUrl',
                      ),
                      const SizedBox(height: 4.0),
                      const Text('읽고싶어요', style: bookStatusStyle),
                    ],
                  ),
                ),
                const SizedBox(height: 8.0),
                Text(
                  '${newInterestCount} 명',
                  style: bookStatusCountStyle,
                ),
              ],
            ),
            bookStatusColumn(
              padding: 36.0,
              children: [
                SvgPicture.asset('assets/svg/icon/small/read.svg'),
                const SizedBox(height: 4.0),
                const Text('완독했어요', style: bookStatusStyle),
                const SizedBox(height: 8.0),
                Text('$readCount 명', style: bookStatusCountStyle),
              ],
            ),
            bookStatusColumn(
              padding: 24.0,
              lastBox: true,
              children: [
                SvgPicture.asset('assets/svg/icon/small/holder.svg'),
                const SizedBox(height: 4.0),
                const Text('소장하고있어요', style: bookStatusStyle),
                const SizedBox(height: 8.0),
                Text('$holderCount 명', style: bookStatusCountStyle),
              ],
            ),
          ],
        ),
        const SizedBox(height: 20.0),
      ],
    );
  }

  Widget bookStatusColumn({
    required double padding,
    bool lastBox = false,
    required List<Widget> children,
  }) {
    return Container(
      decoration: lastBox
          ? null
          : const BoxDecoration(
              border: Border(
                right: BorderSide(
                  color: greyDDDDDD,
                  width: 1,
                ),
              ),
            ),
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: padding),
        child: Column(
          children: children,
        ),
      ),
    );
  }
}
