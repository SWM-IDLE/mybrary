import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

class SearchBookListInfo extends StatelessWidget {
  final List<BookSearchData> searchBookList;
  final ScrollController scrollController;

  const SearchBookListInfo({
    required this.searchBookList,
    required this.scrollController,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Scrollbar(
        child: ListView.builder(
          controller: scrollController,
          itemCount: searchBookList.length,
          itemBuilder: (context, index) {
            final searchBookData = searchBookList[index];
            final DateTime publishDate =
                DateTime.parse(searchBookData.publicationDate!);

            return GestureDetector(
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => SearchDetailScreen(
                      searchBookData: searchBookData,
                    ),
                  ),
                );
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
                                color: BOOK_BORDER_COLOR,
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
                                  infoText: '${searchBookData.authors!.map(
                                        (e) => e,
                                      ).join(', ')} 저',
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
                                SizedBox(
                                  height: 8.0,
                                ),
                                Row(
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Image.asset(
                                      'assets/icon/star_icon.png',
                                      color: BOOK_STAR_COLOR,
                                      width: 20.0,
                                    ),
                                    SizedBox(
                                      width: 6.0,
                                    ),
                                    bookInfo(
                                      infoText: '${searchBookData.starRating!}',
                                      fontSize: 18.0,
                                      fontColor: BOOK_STAR_RATING_COLOR,
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
                  Divider(
                    thickness: 1,
                    height: 1,
                    color: DIVIDER_COLOR,
                  ),
                ],
              ),
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
