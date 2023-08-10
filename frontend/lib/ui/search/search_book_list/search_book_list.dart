import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/constants/color.dart';
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
  late List<BookSearchResult> _bookSearchResultData = [];
  late bool _isScrollLoading = false;

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
      child: NotificationListener<ScrollUpdateNotification>(
        onNotification: (ScrollUpdateNotification notification) {
          if (notification.metrics.maxScrollExtent * 0.85 <
              notification.metrics.pixels) {
            setState(() {
              _isScrollLoading = true;
            });
          } else {
            setState(() {
              _isScrollLoading = false;
            });
          }
          return false;
        },
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
                _bookSearchResultData
                    .addAll(bookSearchResponse.bookSearchResult!);
                _bookSearchNextUrl = bookSearchResponse.nextRequestUrl!;
              }

              if (_bookSearchNextUrl != "" && _isScrollLoading) {
                _searchRepository
                    .getBookSearchResponse(
                      requestUrl:
                          '${getBookServiceApi(API.getBookService)}$_bookSearchNextUrl',
                    )
                    .then((value) => setState(() {
                          _bookSearchResultData.addAll(value.bookSearchResult!);
                          _bookSearchNextUrl = value.nextRequestUrl!;
                        }));

                _bookSearchNextUrl = "";
                _isScrollLoading = false;
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
        onSubmitted: (value) {
          _bookSearchKeywordController.text = value;

          _searchRepository
              .getBookSearchResponse(
                requestUrl:
                    '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
              )
              .then(
                (value) => setState(() {
                  _bookSearchResultData = value.bookSearchResult!;
                  _bookSearchNextUrl = value.nextRequestUrl!;
                  _searchScrollController.animateTo(
                    0,
                    duration: const Duration(microseconds: 500),
                    curve: Curves.easeInOut,
                  );
                }),
              );
        },
        decoration: InputDecoration(
          contentPadding: const EdgeInsets.symmetric(
            vertical: 6.0,
          ),
          hintText: '책, 저자, 회원을 검색해보세요.',
          hintStyle: commonSubRegularStyle,
          filled: true,
          fillColor: commonGreyOpacityColor,
          focusedBorder: searchInputBorderStyle,
          enabledBorder: searchInputBorderStyle,
          focusColor: commonGreyColor,
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
                    color: grey777777,
                    size: 18.0,
                  ),
                )
              : null,
        ),
      ),
    );
  }
}
