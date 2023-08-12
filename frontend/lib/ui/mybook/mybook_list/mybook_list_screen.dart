import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/mybook/mybook_list/components/book_list.dart';

class MyBookListScreen extends StatefulWidget {
  final String bookListTitle;
  final List<MyBooksResponseData> bookList;

  const MyBookListScreen({
    required this.bookListTitle,
    required this.bookList,
    super.key,
  });

  @override
  State<MyBookListScreen> createState() => _MyBookListScreenState();
}

class _MyBookListScreenState extends State<MyBookListScreen> {
  late List<MyBooksResponseData> _bookList;

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
          if (_bookList.isEmpty)
            SliverToBoxAdapter(
              child: SizedBox(
                height: MediaQuery.of(context).size.height / 1.5,
                child: const Center(
                  child: Text('등록된 도서가 없습니다.'),
                ),
              ),
            ),
          if (_bookList.isNotEmpty)
            SliverPadding(
              padding: const EdgeInsets.symmetric(
                horizontal: 16.0,
                vertical: 8.0,
              ),
              sliver: SliverToBoxAdapter(
                child: InkWell(
                  onTap: () {},
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
          if (_bookList.isNotEmpty)
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
