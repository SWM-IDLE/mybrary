import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';

class SearchBookListHeader extends StatelessWidget {
  final List<BookSearchData> searchBookList;
  const SearchBookListHeader({
    required this.searchBookList,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14.0),
      width: double.infinity,
      height: 40.0,
      child: Text(
        // Todo : 전체 검색 데이터 수로 변경 예정
        '검색 도서 ${searchBookList.length}',
        style: TextStyle(
          fontSize: 15.0,
          fontWeight: FontWeight.w700,
        ),
      ),
    );
  }
}
