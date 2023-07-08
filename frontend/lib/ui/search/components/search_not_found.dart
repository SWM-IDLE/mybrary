import 'package:flutter/material.dart';

class SearchNotFound extends StatelessWidget {
  const SearchNotFound({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text('검색 결과가 없습니다.'),
    );
  }
}
