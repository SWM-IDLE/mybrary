import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

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
  final Function(bool) onBookSearchBinding;

  const SearchPopularKeyword({
    required this.bookSearchKeywordController,
    required this.onBookSearchBinding,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 18.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '인기 검색어',
            style: TextStyle(
              fontSize: 15.0,
              fontWeight: FontWeight.w700,
            ),
          ),
          SizedBox(
            height: 16.0,
          ),
          Wrap(
            spacing: 8.0,
            runSpacing: 8.0,
            children: List.generate(
              popularSearchKeyword.length,
              (index) => GestureDetector(
                onTap: () {
                  bookSearchKeywordController.text =
                      popularSearchKeyword[index];
                  onBookSearchBinding(true);
                  FocusManager.instance.primaryFocus?.unfocus();
                },
                child: Container(
                  padding:
                      EdgeInsets.symmetric(horizontal: 18.0, vertical: 8.0),
                  decoration: BoxDecoration(
                    border: Border.all(
                      color: POPULAR_BORDER_COLOR,
                    ),
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                  child: Text(
                    popularSearchKeyword[index],
                    style: TextStyle(
                      fontSize: 14.0,
                      color: POPULAR_TEXT_COLOR,
                    ),
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
