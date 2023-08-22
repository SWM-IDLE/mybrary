import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/home/book_recommendations_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class HomeRecommendBooks extends StatelessWidget {
  final String category;
  final List<UserInterests> userInterests;
  final List<BookRecommendations> bookListByCategory;
  final void Function(String) onTapCategory;
  final void Function(String) onTapBook;
  final ScrollController categoryScrollController;
  final VoidCallback onTapMyInterests;

  const HomeRecommendBooks({
    required this.category,
    required this.userInterests,
    required this.bookListByCategory,
    required this.onTapCategory,
    required this.onTapBook,
    required this.categoryScrollController,
    required this.onTapMyInterests,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.only(
            left: 16.0,
            bottom: 12.0,
          ),
          child: Row(
            children: [
              Text(
                '추천 도서, ',
                style: commonSubBoldStyle,
              ),
              Text(
                '이건 어때요?',
                style: commonMainRegularStyle,
              ),
            ],
          ),
        ),
        if (bookListByCategory.isEmpty)
          InkWell(
            onTap: () {
              onTapMyInterests();
            },
            child: Padding(
              padding: const EdgeInsets.symmetric(
                horizontal: 16.0,
                vertical: 4.0,
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    '지금 바로 마이 관심사를 등록해보세요!',
                    style: commonSubRegularStyle.copyWith(
                      fontSize: 15.0,
                      decoration: TextDecoration.underline,
                    ),
                  ),
                  SizedBox(
                    child: SvgPicture.asset(
                      'assets/svg/icon/right_arrow.svg',
                      width: 14.0,
                      height: 14.0,
                    ),
                  ),
                ],
              ),
            ),
          ),
        if (bookListByCategory.isNotEmpty)
          Padding(
            padding: const EdgeInsets.symmetric(
              horizontal: 16.0,
              vertical: 8.0,
            ),
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: List.generate(
                userInterests.length,
                (index) => InkWell(
                  onTap: () {
                    onTapCategory(userInterests[index].name!);
                  },
                  child: Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 19.0,
                      vertical: 7.0,
                    ),
                    decoration: BoxDecoration(
                      color: category == userInterests[index].name!
                          ? grey262626
                          : commonWhiteColor,
                      border: Border.all(
                        color: grey777777,
                      ),
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                    child: Text(
                      userInterests[index].name!,
                      style: categoryCircularTextStyle.copyWith(
                        color: category == userInterests[index].name!
                            ? commonWhiteColor
                            : grey262626,
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
        Expanded(
          child: ListView.builder(
            controller: categoryScrollController,
            scrollDirection: Axis.horizontal,
            physics: const BouncingScrollPhysics(),
            itemCount: bookListByCategory.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(
                  top: 10.0,
                  right: 10.0,
                  bottom: 10.0,
                ),
                child: Row(
                  children: [
                    if (index == 0)
                      const SizedBox(
                        width: 16.0,
                      ),
                    InkWell(
                      onTap: () {
                        onTapBook(bookListByCategory[index].isbn13!);
                      },
                      child: Container(
                        width: 116,
                        decoration: BoxDecoration(
                          image: DecorationImage(
                            image: NetworkImage(
                              bookListByCategory[index].thumbnailUrl!,
                            ),
                            fit: BoxFit.fill,
                          ),
                          borderRadius: BorderRadius.circular(8),
                          boxShadow: const [
                            BoxShadow(
                              color: Color(0x3F000000),
                              blurRadius: 2,
                              offset: Offset(1, 1),
                              spreadRadius: 1,
                            )
                          ],
                        ),
                      ),
                    ),
                    if (index == bookListByCategory.length - 1)
                      const SizedBox(
                        width: 8.0,
                      ),
                  ],
                ),
              );
            },
          ),
        ),
      ],
    );
  }
}
