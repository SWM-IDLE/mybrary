import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';

class SearchHeader extends StatelessWidget {
  final TextEditingController bookSearchController;
  final ValueChanged onSubmittedSearchKeyword;

  const SearchHeader({
    required this.bookSearchController,
    required this.onSubmittedSearchKeyword,
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
        Expanded(
          child: Padding(
            padding: EdgeInsets.symmetric(
              vertical: 14.0,
              horizontal: 18.0,
            ),
            child: TextField(
              textInputAction: TextInputAction.search,
              controller: bookSearchController,
              cursorColor: primaryColor,
              onSubmitted: (value) => onSubmittedSearchKeyword(value),
              decoration: InputDecoration(
                contentPadding: const EdgeInsets.symmetric(
                  vertical: 6.0,
                ),
                hintText: '책, 저자, 회원을 검색해보세요.',
                hintStyle: TextStyle(
                  fontSize: 14.0,
                ),
                filled: true,
                fillColor: commonGreyOpacityColor,
                focusedBorder: searchInputBorderStyle,
                enabledBorder: searchInputBorderStyle,
                focusColor: commonGreyColor,
                prefixIcon: SvgPicture.asset(
                  'assets/svg/icon/search_small.svg',
                  fit: BoxFit.scaleDown,
                ),
                suffixIcon: bookSearchController.text.isNotEmpty
                    ? IconButton(
                        onPressed: () {
                          bookSearchController.clear();
                        },
                        icon: const Icon(
                          Icons.cancel_rounded,
                          color: grey777777,
                          size: 18.0,
                        ),
                      )
                    : null,
              ),
            ),
          ),
        ),
      ],
    );
  }
}
