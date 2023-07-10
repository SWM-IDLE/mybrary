import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book_search_data.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchBookList extends StatelessWidget {
  // final int searchBookTotalLength;
  final List<BookSearchData> searchBookList;
  final ScrollController scrollController;
  const SearchBookList({
    required this.searchBookList,
    required this.scrollController,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 14.0),
            width: double.infinity,
            height: 40.0,
            child: Text(
              '검색 도서 ${searchBookList.length}',
              style: TextStyle(
                fontSize: 15.0,
                fontWeight: FontWeight.w700,
              ),
            ),
          ),
          Expanded(
            child: ListView.builder(
              controller: scrollController,
              itemCount: searchBookList.length,
              itemBuilder: (context, index) {
                final searchBookData = searchBookList[index];
                final DateTime publicationDate =
                    DateTime.parse(searchBookData.publicationDate!);

                return Column(
                  children: [
                    Padding(
                      padding: EdgeInsets.only(
                        left: 16.0,
                        right: 16.0,
                        top: index == 0 ? 5.0 : 20.0,
                        bottom: 20.0,
                      ),
                      child: Row(
                        children: [
                          Container(
                            width: 80,
                            height: 120,
                            decoration: BoxDecoration(
                              border: Border.all(
                                color: Colors.grey.withOpacity(0.2),
                              ),
                              borderRadius: BorderRadius.circular(10.0),
                            ),
                            child: Image.network(
                              searchBookData.thumbnailUrl!,
                              fit: BoxFit.cover,
                            ),
                          ),
                          SizedBox(
                            width: 12.0,
                          ),
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(searchBookData.title!,
                                    style: TextStyle(
                                      fontSize: 16.0,
                                      fontWeight: FontWeight.bold,
                                    ),
                                    textWidthBasis: TextWidthBasis.parent),
                                SizedBox(
                                  height: 4.0,
                                ),
                                Text(
                                  '${searchBookData.publisher!} 저',
                                  style: TextStyle(
                                    fontSize: 14.0,
                                    color: DESCRIPTION_GREY_COLOR,
                                  ),
                                ),
                                SizedBox(
                                  height: 4.0,
                                ),
                                Text(
                                  '${publicationDate.year}.${publicationDate.month}',
                                  style: TextStyle(
                                    fontSize: 13.0,
                                    color: GREY_COLOR,
                                  ),
                                ),
                                SizedBox(
                                  height: 6.0,
                                ),
                                Row(
                                  children: [
                                    Icon(
                                      Icons.star,
                                      color: BOOK_STAR_COLOR,
                                      size: 20.0,
                                    ),
                                    SizedBox(
                                      width: 4.0,
                                    ),
                                    Text(
                                      '${searchBookData.starRating!}',
                                      style: TextStyle(
                                        fontSize: 14.0,
                                        color: GREY_COLOR,
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                    Divider(
                      thickness: 1,
                      height: 1,
                      color: DIVIDER_COLOR,
                    ),
                  ],
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
