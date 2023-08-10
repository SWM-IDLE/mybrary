import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/mybook/book_list/components/book_list.dart';

class BookListScreen extends StatefulWidget {
  final String bookListTitle;
  final List<BookListResponseData> bookList;

  const BookListScreen({
    required this.bookListTitle,
    required this.bookList,
    super.key,
  });

  @override
  State<BookListScreen> createState() => _BookListScreenState();
}

class _BookListScreenState extends State<BookListScreen> {
  late List<BookListResponseData> _bookList;

  @override
  void initState() {
    super.initState();

    _bookList = widget.bookList;
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      child: CustomScrollView(
        physics: const BouncingScrollPhysics(
          parent: AlwaysScrollableScrollPhysics(),
        ),
        slivers: [
          _bookListAppBar(appBarTitle: widget.bookListTitle),
          SliverPadding(
            padding: const EdgeInsets.symmetric(
              horizontal: 16.0,
              vertical: 8.0,
            ),
            sliver: SliverToBoxAdapter(
              child: InkWell(
                onTap: () {
                  print('sort1');
                },
                child: Row(
                  children: [
                    Text(
                      '전체 ${_bookList.length}권',
                      style: myBookSortTextStyle,
                    ),
                    const SizedBox(width: 4.0),
                    const Icon(
                      Icons.arrow_drop_down_outlined,
                    ),
                  ],
                ),
              ),
            ),
          ),
          BookList(
            bookList: widget.bookList,
          ),
        ],
      ),
    );
  }

  SliverAppBar _bookListAppBar({
    required String appBarTitle,
    List<Widget>? appBarActions,
  }) {
    return SliverAppBar(
      elevation: 0,
      title: Text(appBarTitle),
      titleTextStyle: appBarTitleStyle.copyWith(
        fontSize: 16.0,
      ),
      pinned: true,
      centerTitle: true,
      backgroundColor: commonWhiteColor,
      foregroundColor: commonBlackColor,
      actions: appBarActions,
    );
  }
}
