import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/search/search_book_list/search_book_list.dart';

const List<String> popularSearchKeyword = [
  '돈의 속성',
  '코스모스',
  '미움 받을 용기',
  '데미안',
  '어린왕자',
  '봉주르뚜르',
  '함께 자라기',
  '나미야',
];

class SearchPopularKeyword extends StatelessWidget {
  final TextEditingController bookSearchKeywordController;

  const SearchPopularKeyword({
    required this.bookSearchKeywordController,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '인기 검색어',
            style: TextStyle(
              fontSize: 15.0,
              fontWeight: FontWeight.w700,
            ),
          ),
          const SizedBox(height: 16.0),
          Wrap(
            spacing: 8.0,
            runSpacing: 8.0,
            children: List.generate(
              popularSearchKeyword.length,
              (index) => InkWell(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => SearchBookList(
                        searchKeyword: popularSearchKeyword[index],
                      ),
                    ),
                  ).then(
                    (value) => bookSearchKeywordController.clear(),
                  );
                },
                child: Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 18.0,
                    vertical: 8.0,
                  ),
                  decoration: BoxDecoration(
                    border: Border.all(
                      color: greyDDDDDD,
                    ),
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                  child: Text(
                    popularSearchKeyword[index],
                    style: popularKeywordTextStyle,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
