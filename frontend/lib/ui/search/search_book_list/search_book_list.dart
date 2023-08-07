import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/search/components/search_loading.dart';
import 'package:mybrary/ui/search/components/search_not_found.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_header.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_info.dart';

class SearchBookList extends StatefulWidget {
  final String bookSearchKeyword;

  const SearchBookList({
    required this.bookSearchKeyword,
    super.key,
  });

  @override
  State<SearchBookList> createState() => _SearchBookListState();
}

class _SearchBookListState extends State<SearchBookList> {
  final _searchRepository = SearchRepository();

  late bool _isClearButtonVisible = false;
  late String _bookSearchNextUrl;
  final String _bookSearchKeywordRequestUrl =
      getBookServiceApi(API.getBookSearchKeyword);
  late Future<BookSearchResponseData> _bookSearchResponse;
  late final List<BookSearchResult> _bookSearchResultData = [];

  final TextEditingController _bookSearchKeywordController =
      TextEditingController();
  final ScrollController _searchScrollController = ScrollController();

  @override
  void initState() {
    super.initState();

    _bookSearchKeywordController.text = widget.bookSearchKeyword;
    _isClearButtonVisible = true;
    _bookSearchResponse = _searchRepository.getBookSearchResponse(
      requestUrl:
          '$_bookSearchKeywordRequestUrl?keyword=${widget.bookSearchKeyword}',
    );

    _searchScrollController.addListener(() async {
      ScrollPosition scrollPosition = _searchScrollController.position;
      if (_searchScrollController.offset >
              scrollPosition.maxScrollExtent * 0.85 &&
          !scrollPosition.outOfRange) {
        if (_bookSearchNextUrl != "") {
          BookSearchResponseData additionalBookSearchResponse =
              await _searchRepository.getBookSearchResponse(
            requestUrl:
                '${getBookServiceApi(API.getBookService)}$_bookSearchNextUrl',
          );

          setState(() {
            _saveBookSearchResultDataAndNextRequestUrl(
              bookSearchResult: additionalBookSearchResponse.bookSearchResult!,
              nextRequestUrl: additionalBookSearchResponse.nextRequestUrl!,
            );
          });

          _bookSearchNextUrl = "";
        }
      }
    });
  }

  @override
  void dispose() {
    _bookSearchKeywordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '검색',
      child: FutureBuilder<BookSearchResponseData>(
        future: _bookSearchResponse,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Column(
              children: [
                searchInputBox(),
                const SearchNotFound(),
              ],
            );
          }

          if (!snapshot.hasData) {
            return const SearchLoading();
          }

          if (snapshot.hasData) {
            BookSearchResponseData bookSearchResponse = snapshot.data!;

            if (_bookSearchResultData.isEmpty) {
              _saveBookSearchResultDataAndNextRequestUrl(
                bookSearchResult: bookSearchResponse.bookSearchResult!,
                nextRequestUrl: bookSearchResponse.nextRequestUrl!,
              );
            }

            return Column(
              children: [
                searchInputBox(),
                SearchBookListHeader(
                  keyword: _bookSearchKeywordController.text,
                  bookSearchDataList: _bookSearchResultData,
                ),
                SearchBookListInfo(
                  bookSearchDataList: _bookSearchResultData,
                  scrollController: _searchScrollController,
                ),
              ],
            );
          }

          return const SearchNotFound();
        },
      ),
    );
  }

  Padding searchInputBox() {
    return Padding(
      padding: const EdgeInsets.only(
        top: 0.0,
        left: 18.0,
        bottom: 12.0,
        right: 18.0,
      ),
      child: TextField(
        textInputAction: TextInputAction.search,
        controller: _bookSearchKeywordController,
        cursorColor: primaryColor,
        onChanged: (value) {
          setState(() {
            _isClearButtonVisible = value.isNotEmpty;
          });
        },
        onSubmitted: _onSubmittedSearchKeyword,
        decoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(
            vertical: 6.0,
          ),
          hintText: '책, 저자, 회원을 검색해보세요.',
          hintStyle: commonSubRegularStyle,
          filled: true,
          fillColor: GREY_COLOR_OPACITY_TWO,
          focusedBorder: searchInputBorderStyle,
          enabledBorder: searchInputBorderStyle,
          focusColor: GREY_COLOR,
          prefixIcon: SvgPicture.asset(
            'assets/svg/icon/search_small.svg',
            fit: BoxFit.scaleDown,
          ),
          suffixIcon: _isClearButtonVisible
              ? IconButton(
                  onPressed: () {
                    setState(() {
                      _bookSearchKeywordController.text = '';
                      _isClearButtonVisible = false;
                    });
                  },
                  icon: const Icon(
                    Icons.cancel_rounded,
                    color: GREY_05_COLOR,
                    size: 18.0,
                  ),
                )
              : null,
        ),
      ),
    );
  }

  void _saveBookSearchResultDataAndNextRequestUrl({
    required List<BookSearchResult> bookSearchResult,
    required String nextRequestUrl,
  }) {
    _bookSearchResultData.addAll(bookSearchResult);
    _bookSearchNextUrl = nextRequestUrl;
  }

  void _onSubmittedSearchKeyword(value) {
    setState(() {
      _bookSearchKeywordController.text = value;
      _bookSearchResponse = _searchRepository.getBookSearchResponse(
        requestUrl:
            '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
      );
    });
  }
}
