import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class SearchHeader extends StatelessWidget {
  final bool isSearching;
  final TextEditingController bookSearchController;
  final ValueChanged onSubmittedSearchKeyword;
  final VoidCallback onTextClearPressed;
  final VoidCallback onSearchCancelPressed;
  const SearchHeader({
    required this.isSearching,
    required this.bookSearchController,
    required this.onSubmittedSearchKeyword,
    required this.onTextClearPressed,
    required this.onSearchCancelPressed,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final searchInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(5.0),
    );
    return Row(
      children: [
        IconButton(
          onPressed: () {
            Navigator.of(context)
                .pushNamedAndRemoveUntil('/home', (route) => false);
          },
          icon: Icon(
            Icons.arrow_back,
            color: GREY_COLOR,
            size: 22.0,
          ),
        ),
        Expanded(
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 14.0),
            child: TextField(
              autofocus: true,
              textInputAction: TextInputAction.search,
              controller: bookSearchController,
              cursorColor: PRIMARY_COLOR,
              onSubmitted: (value) => onSubmittedSearchKeyword(value),
              decoration: InputDecoration(
                contentPadding: EdgeInsets.symmetric(vertical: 6.0),
                hintText: '책, 저자, 회원을 검색해보세요.',
                hintStyle: TextStyle(
                  fontSize: 14.0,
                ),
                filled: true,
                fillColor: GREY_COLOR_OPACITY_TWO,
                focusedBorder: searchInputBorderStyle,
                enabledBorder: searchInputBorderStyle,
                focusColor: GREY_COLOR,
                prefixIcon: Image.asset(
                  'assets/icon/search_icon.png',
                  color: LESS_GREY_COLOR,
                  scale: 1.2,
                ),
                suffixIcon: IconButton(
                  onPressed: onTextClearPressed,
                  icon: Icon(
                    Icons.clear,
                    color: GREY_COLOR,
                    size: 22.0,
                  ),
                ),
              ),
            ),
          ),
        ),
        if (!isSearching)
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            child: IconButton(
              onPressed: () {
                Navigator.of(context).pushNamed('/search/barcode');
              },
              icon: Image.asset(
                'assets/icon/barcode_scan_icon.png',
                color: PRIMARY_COLOR,
              ),
            ),
          )
        else
          TextButton(
            onPressed: onSearchCancelPressed,
            child: Text(
              '취소',
              style: TextStyle(
                color: DESCRIPTION_GREY_COLOR,
                fontSize: 14.0,
                fontWeight: FontWeight.w600,
              ),
            ),
          )
      ],
    );
  }
}
