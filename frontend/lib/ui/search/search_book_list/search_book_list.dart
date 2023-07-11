import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_header.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_info.dart';

class SearchBookList extends StatefulWidget {
  final List<BookSearchData> searchBookList;
  final ScrollController scrollController;
  const SearchBookList({
    required this.searchBookList,
    required this.scrollController,
    super.key,
  });

  @override
  State<SearchBookList> createState() => _SearchBookListState();
}

class _SearchBookListState extends State<SearchBookList> {
  // Todo : 전체 선택 또는 일부 선택을 통한 책 설정

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Column(
        children: [
          SearchBookListHeader(
            searchBookList: widget.searchBookList,
          ),
          SearchBookListInfo(
            searchBookList: widget.searchBookList,
            scrollController: widget.scrollController,
          ),
        ],
      ),
    );
  }
}
