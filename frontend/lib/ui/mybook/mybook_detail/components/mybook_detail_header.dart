import 'package:flutter/material.dart';
import 'package:html/parser.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class MyBookDetailHeader extends StatelessWidget {
  final String thumbnail;
  final String title;
  final List<String> authors;
  final GlobalKey? headerKey;

  const MyBookDetailHeader({
    required this.thumbnail,
    required this.title,
    required this.authors,
    this.headerKey,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SliverToBoxAdapter(
      child: Column(
        key: headerKey,
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
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 6.0),
                Text(
                  authors.map((author) => author).join(', '),
                  style: commonSubRegularStyle.copyWith(
                    color: bookDescriptionColor,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 20.0),
        ],
      ),
    );
  }
}
