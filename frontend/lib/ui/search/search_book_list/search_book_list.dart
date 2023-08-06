import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/datasource/search/search_datasource.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
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
  final SearchDataSource _searchDataSource = SearchDataSource();

  late Future<BookSearchResponseData> _bookSearchResponse;
  late final List<BookSearchResult> _bookSearchResultData = [];
  late String _bookSearchNextUrl;

  final TextEditingController _bookSearchKeywordController =
      TextEditingController();
  final ScrollController _searchScrollController = ScrollController();

  @override
  void initState() {
    super.initState();

    _bookSearchKeywordController.text = widget.bookSearchKeyword;
    _bookSearchResponse = _searchDataSource.getBookSearchResponse(
        '${getBookServiceApi(API.getBookSearchKeyword)}?keyword=${widget.bookSearchKeyword}');
  }

  @override
  void setState(VoidCallback fn) {
    super.setState(fn);

    // 스크롤 맨 하단에 닿기 바로 이전에 실행 (max x 0.85)
    _searchScrollController.addListener(() async {
      ScrollPosition scrollPosition = _searchScrollController.position;
      if (_searchScrollController.offset >
              scrollPosition.maxScrollExtent * 0.85 &&
          !scrollPosition.outOfRange) {
        if (_bookSearchNextUrl != "") {
          // nextUrl 이 있을 때 추가 데이터 호출
          _fetchBookSearchNextUrlResponse();
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
            return const SearchNotFound();
          }

          if (!snapshot.hasData) {
            return const SearchLoading();
          }

          if (snapshot.hasData) {
            BookSearchResponseData bookSearchResponse = snapshot.data!;
            String bookSearchNextRequestUrl =
                bookSearchResponse.nextRequestUrl!;

            if (_bookSearchResultData.isEmpty) {
              _bookSearchResultData
                  .addAll(bookSearchResponse.bookSearchResult!);
              _bookSearchNextUrl = bookSearchNextRequestUrl;
            }

            return Column(
              children: [
                Padding(
                  padding: const EdgeInsets.symmetric(
                    vertical: 14.0,
                    horizontal: 18.0,
                  ),
                  child: TextField(
                    textInputAction: TextInputAction.search,
                    controller: _bookSearchKeywordController,
                    cursorColor: primaryColor,
                    onSubmitted: (value) {
                      setState(() {
                        _bookSearchKeywordController.text = value;
                        _bookSearchResponse =
                            _searchDataSource.getBookSearchResponse(
                                '${getBookServiceApi(API.getBookSearchKeyword)}?keyword=${_bookSearchKeywordController.text}');
                      });
                    },
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
                      suffixIcon: _bookSearchKeywordController.text.isNotEmpty
                          ? IconButton(
                              onPressed: () {
                                _bookSearchKeywordController.clear();
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
                ),
                SearchBookListHeader(
                    bookSearchDataList: bookSearchResponse.bookSearchResult!),
                SearchBookListInfo(
                  bookSearchDataList: bookSearchResponse.bookSearchResult!,
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

  Future<void> _fetchBookSearchNextUrlResponse() async {
    BookSearchResponseData additionalBookSearchResponse =
        await _searchDataSource.getBookSearchResponse(
            '${getBookServiceApi(API.getBookService)}$_bookSearchNextUrl');

    setState(() {
      _bookSearchResultData
          .addAll(additionalBookSearchResponse.bookSearchResult!);
      _bookSearchNextUrl = additionalBookSearchResponse.nextRequestUrl!;
    });
  }
}
