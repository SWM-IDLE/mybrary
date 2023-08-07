import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class SearchBookListInfo extends StatelessWidget {
  final List<BookSearchResult> bookSearchDataList;
  final ScrollController scrollController;

  const SearchBookListInfo({
    required this.bookSearchDataList,
    required this.scrollController,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Scrollbar(
        controller: scrollController,
        child: ListView.separated(
          physics: const BouncingScrollPhysics(),
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
                                color: GREY_01_COLOR,
                                width: 1,
                              ),
                              borderRadius: BorderRadius.circular(8.0),
                              image: DecorationImage(
                                image: NetworkImage(
                                  searchBookData.thumbnailUrl!,
                                ),
                                fit: BoxFit.fill,
                              ),
                            ),
                          ),
                          const SizedBox(width: 12.0),
                          Expanded(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      searchBookData.title!,
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                      textWidthBasis: TextWidthBasis.parent,
                                      style: searchBookTitleStyle,
                                    ),
                                    const SizedBox(height: 4.0),
                                    bookInfo(
                                      infoText: searchBookData.description!,
                                      fontSize: 13.0,
                                      fontColor: BOOK_DESCRIPTION_COLOR,
                                    ),
                                  ],
                                ),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    bookInfo(
                                      infoText: searchBookData.author!,
                                      fontSize: 13.0,
                                      fontColor: BOOK_DESCRIPTION_COLOR,
                                    ),
                                    const SizedBox(height: 1.0),
                                    bookInfo(
                                      infoText:
                                          '${publishDate.year}.${publishDate.month}',
                                      fontSize: 13.0,
                                      fontColor: BOOK_DESCRIPTION_COLOR,
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
              color: GREY_01_COLOR,
            );
          },
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
        color: fontColor ?? BLACK_COLOR,
        fontSize: fontSize,
        fontWeight: fontWeight ?? FontWeight.w500,
      ),
    );
  }
}
