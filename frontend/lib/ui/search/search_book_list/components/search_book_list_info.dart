import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/res/colors/color.dart';
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
          controller: scrollController,
          itemCount: bookSearchDataList.length,
          itemBuilder: (context, index) {
            final searchBookData = bookSearchDataList[index];
            final DateTime publishDate =
                getPublishDate(searchBookData.publicationDate!);

            return GestureDetector(
              onTap: () {
                // Navigator.push(
                //   context,
                //   MaterialPageRoute(
                //     builder: (_) => SearchDetailScreen(
                //       bookSearchData: searchBookData,
                //     ),
                //   ),
                // );
              },
              behavior: HitTestBehavior.opaque,
              child: Column(
                children: [
                  Padding(
                    padding: EdgeInsets.only(
                      left: 20.0,
                      right: 20.0,
                      top: index == 0 ? 5.0 : 20.0,
                      bottom: 20.0,
                    ),
                    child: Container(
                      height: 140,
                      child: Row(
                        children: [
                          Container(
                            width: 100,
                            height: 140,
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
                          SizedBox(
                            width: 12.0,
                          ),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                bookInfo(
                                  infoText: searchBookData.title!,
                                  fontSize: 16.0,
                                  fontWeight: FontWeight.w700,
                                ),
                                SizedBox(
                                  height: 4.0,
                                ),
                                bookInfo(
                                  infoText: searchBookData.author!,
                                  fontSize: 14.0,
                                  fontColor: BOOK_DESCRIPTION_COLOR,
                                ),
                                SizedBox(
                                  height: 4.0,
                                ),
                                bookInfo(
                                  infoText:
                                      '${publishDate.year}.${publishDate.month}',
                                  fontSize: 13.0,
                                  fontColor: BOOK_DESCRIPTION_COLOR,
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
      maxLines: 2,
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
