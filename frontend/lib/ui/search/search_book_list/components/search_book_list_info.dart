import 'package:flutter/material.dart';
import 'package:html/parser.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class SearchBookListInfo extends StatelessWidget {
  final List<BookSearchResult> bookSearchDataList;
  final ScrollController scrollController;
  final double paddingTopHeight;

  const SearchBookListInfo({
    required this.bookSearchDataList,
    required this.scrollController,
    required this.paddingTopHeight,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Padding(
        padding: EdgeInsets.only(top: paddingTopHeight),
        child: Scrollbar(
          controller: scrollController,
          child: ListView.separated(
            physics: const BouncingScrollPhysics(
              parent: AlwaysScrollableScrollPhysics(),
            ),
            keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
            controller: scrollController,
            itemCount: bookSearchDataList.length,
            itemBuilder: (context, index) {
              final searchBookData = bookSearchDataList[index];
              final DateTime publishDate =
                  getPublishDate(searchBookData.publicationDate!);

              return GestureDetector(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => SearchDetailScreen(
                        isbn13: searchBookData.isbn13!,
                      ),
                    ),
                  );
                },
                behavior: HitTestBehavior.opaque,
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: SizedBox(
                        height: 126,
                        child: Row(
                          children: [
                            Container(
                              width: 86,
                              height: 126,
                              decoration: BoxDecoration(
                                border: Border.all(
                                  color: greyF1F2F5,
                                  width: 1,
                                ),
                                borderRadius: BorderRadius.circular(8.0),
                                image: DecorationImage(
                                  image: NetworkImage(
                                    searchBookData.thumbnailUrl!,
                                  ),
                                  onError: (exception, stackTrace) =>
                                      Image.asset(
                                    'assets/img/logo/mybrary.png',
                                    fit: BoxFit.fill,
                                  ),
                                  fit: BoxFit.fill,
                                ),
                              ),
                            ),
                            const SizedBox(width: 12.0),
                            Expanded(
                              child: Column(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        parse(searchBookData.title!)
                                            .documentElement!
                                            .text,
                                        maxLines: 2,
                                        overflow: TextOverflow.ellipsis,
                                        textWidthBasis: TextWidthBasis.parent,
                                        style: searchBookTitleStyle,
                                      ),
                                      const SizedBox(height: 4.0),
                                      bookInfo(
                                        infoText:
                                            parse(searchBookData.description!)
                                                .documentElement!
                                                .text,
                                        fontSize: 13.0,
                                        fontColor: bookDescriptionColor,
                                      ),
                                    ],
                                  ),
                                  Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      bookInfo(
                                        infoText: searchBookData.author!,
                                        fontSize: 13.0,
                                        fontColor: bookDescriptionColor,
                                      ),
                                      const SizedBox(height: 1.0),
                                      bookInfo(
                                        infoText:
                                            '${publishDate.year}.${publishDate.month}',
                                        fontSize: 13.0,
                                        fontColor: bookDescriptionColor,
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              );
            },
            separatorBuilder: (BuildContext context, int index) {
              return const Divider(
                thickness: 1,
                height: 1,
                color: greyF1F2F5,
              );
            },
          ),
        ),
      ),
    );
  }

  Widget bookInfo({
    required String infoText,
    required double fontSize,
    Color? fontColor,
    FontWeight? fontWeight,
  }) {
    return Text(
      infoText,
      maxLines: 1,
      overflow: TextOverflow.ellipsis,
      textWidthBasis: TextWidthBasis.parent,
      style: TextStyle(
        color: fontColor ?? commonBlackColor,
        fontSize: fontSize,
        fontWeight: fontWeight ?? FontWeight.w500,
      ),
    );
  }
}
