import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

class InterestBookList extends StatelessWidget {
  final List<BookListResponseData> bookList;

  const InterestBookList({
    required this.bookList,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SliverPadding(
      padding: const EdgeInsets.all(12.0),
      sliver: SliverGrid(
        delegate: SliverChildBuilderDelegate(
          (context, index) => InkWell(
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => SearchDetailScreen(
                    isbn13: bookList[index].isbn13!,
                  ),
                ),
              );
            },
            child: Column(
              children: [
                Expanded(
                  flex: 3,
                  child: Container(
                    decoration: ShapeDecoration(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                      image: DecorationImage(
                        image: NetworkImage(
                          bookList[index].thumbnailUrl!,
                        ),
                        fit: BoxFit.fill,
                      ),
                      shadows: [
                        BoxShadow(
                          color: commonBlackColor.withOpacity(0.3),
                          blurRadius: 4,
                          offset: const Offset(1, 1),
                          spreadRadius: 1,
                        )
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 12.0),
                Expanded(
                  flex: 1,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        bookList[index].title!,
                        style: myBookListTitleStyle,
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 2.0),
                      Text(
                        bookList[index].author!,
                        style: myBookListSubStyle,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          childCount: bookList.length,
        ),
        gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
          mainAxisExtent: 240,
          maxCrossAxisExtent: 140,
          mainAxisSpacing: 16.0,
          crossAxisSpacing: 10.0,
        ),
      ),
    );
  }
}
