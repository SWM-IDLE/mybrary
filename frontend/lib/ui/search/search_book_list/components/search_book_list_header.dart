import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class SearchBookListHeader extends StatelessWidget {
  final String keyword;
  final List<BookSearchResult> bookSearchDataList;

  const SearchBookListHeader({
    required this.keyword,
    required this.bookSearchDataList,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: 14.0,
        vertical: 14.0,
      ),
      width: double.infinity,
      decoration: const BoxDecoration(
        color: greyF1F2F5,
      ),
      child: const Text(
        '검색 결과',
        style: commonButtonTextStyle,
      ),
    );
  }
}
