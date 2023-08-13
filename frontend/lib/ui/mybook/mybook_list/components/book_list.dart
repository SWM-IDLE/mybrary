import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class BookList extends StatelessWidget {
  final List<MyBooksResponseData> bookList;
  final void Function(int, String)? onTapMyBookDetail;
  final String readStatus;

  const BookList({
    required this.bookList,
    required this.readStatus,
    this.onTapMyBookDetail,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SliverPadding(
      padding: const EdgeInsets.all(12.0),
      sliver: SliverGrid(
        delegate: SliverChildBuilderDelegate(
          (context, index) => InkWell(
            onTap: () => onTapMyBookDetail!(
              bookList[index].id!,
              readStatus,
            ),
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
                          bookList[index].book!.thumbnailUrl!,
                        ),
                        fit: BoxFit.fill,
                      ),
                      shadows: [
                        BoxShadow(
                          color: commonBlackColor.withOpacity(0.3),
                          blurRadius: 2,
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
                  child: SizedBox(
                    width: double.infinity,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          bookList[index].book!.title!,
                          style: myBookListTitleStyle,
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                          textAlign: TextAlign.left,
                        ),
                        const SizedBox(height: 2.0),
                        Text(
                          bookList[index].book!.authors!,
                          style: myBookListSubStyle,
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
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
