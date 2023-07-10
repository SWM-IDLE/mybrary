import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/data/model/book_search_data.dart';
import 'package:mybrary/data/model/book_search_response.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/search/components/search_book_list.dart';
import 'package:mybrary/ui/search/components/search_loading.dart';
import 'package:mybrary/ui/search/components/search_popular_keyword.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  late Future<BookSearchResponse>? _bookSearchResponse;
  late final List<BookSearchData> _bookSearchData = [];
  late String _bookNextRequestUrl;

  bool _isSearching = false;

  final ScrollController _scrollController = ScrollController();
  final TextEditingController _bookSearchController = TextEditingController();

  @override
  void setState(VoidCallback fn) {
    super.setState(fn);
    // 스크롤 맨 하단에 닿기 바로 이전 (* 0.85) nextUrl 이 있을 때 추가 데이터 호출
    _scrollController.addListener(() async {
      ScrollPosition scrollPosition = _scrollController.position;
      if (_scrollController.offset > scrollPosition.maxScrollExtent * 0.85 &&
          !scrollPosition.outOfRange) {
        if (_bookNextRequestUrl != "") {
          _fetchNextBookSearchResponse();
          _bookNextRequestUrl = "";
        }
      }
    });
  }

  @override
  void dispose() {
    _bookSearchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final searchInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(5.0),
    );
    return Scaffold(
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
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
                      controller: _bookSearchController,
                      cursorColor: PRIMARY_COLOR,
                      onSubmitted: (value) {
                        setState(() {
                          _bookSearchController.text = value;
                          _bookSearchResponse = _fetchBookSearchResponse();
                          _isSearching = true;
                        });
                      },
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.symmetric(vertical: 12.0),
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
                          'assets/images/search_icon.png',
                          color: LESS_GREY_COLOR,
                          scale: 1.2,
                        ),
                        suffixIcon: IconButton(
                          onPressed: () {
                            setState(() {
                              _bookSearchController.clear();
                              _bookSearchResponse = null;
                            });
                          },
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
                if (!_isSearching)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 8.0),
                    child: IconButton(
                      onPressed: () {},
                      icon: Image.asset(
                        'assets/images/barcode_scan_icon.png',
                        color: PRIMARY_COLOR,
                      ),
                    ),
                  )
                else
                  TextButton(
                    onPressed: () {
                      setState(() {
                        _bookSearchController.clear();
                        _bookSearchData.clear();
                        _isSearching = false;
                      });
                    },
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
            ),
            SizedBox(
              height: 8.0,
            ),
            if (!_isSearching)
              SearchPopularKeyword(
                bookSearchController: _bookSearchController,
                onBookSearchBinding: getBookSearchPopularKeywordData,
              )
            else
              FutureBuilder<BookSearchResponse>(
                future: _bookSearchResponse,
                builder: (context, snapshot) {
                  // 서버 요청에 문제가 있을 경우
                  if (snapshot.hasError) {
                    return Center(
                      child: Text('에러가 있습니다.'),
                    );
                  }

                  // 서버 요청을 기다리는 경우
                  if (!snapshot.hasData) {
                    return SearchLoading();
                  }

                  // 서버 요청이 완료된 경우
                  if (snapshot.hasData) {
                    BookSearchResponse bookSearchResponse = snapshot.data!;
                    String bookSearchNextRequestUrl =
                        bookSearchResponse.data!.nextRequestUrl!;

                    // 초기 검색 후 데이터 저장
                    if (_bookSearchData.isEmpty) {
                      _bookSearchData
                          .addAll(bookSearchResponse.data!.bookSearchResult!);
                      _bookNextRequestUrl = bookSearchNextRequestUrl;
                    }

                    return SearchBookList(
                      searchBookList: _bookSearchData,
                      scrollController: _scrollController,
                    );
                  }

                  return Container();
                },
              ),
          ],
        ),
      ),
    );
  }

  void getBookSearchPopularKeywordData(bool isBinding) {
    final popularKeyword = _bookSearchController.text;
    setState(() {
      if (popularKeyword != "") {
        isBinding = true;
        _bookSearchResponse =
            RemoteDataSource.getBookSearchKeywordResponse(popularKeyword);
        _isSearching = true;
      }
    });
  }

  Future<BookSearchResponse> _fetchBookSearchResponse() async {
    BookSearchResponse bookSearchResponse =
        await RemoteDataSource.getBookSearchKeywordResponse(
            _bookSearchController.text);

    return bookSearchResponse;
  }

  Future<void> _fetchNextBookSearchResponse() async {
    BookSearchResponse additionalBookSearchResponse =
        await RemoteDataSource.getBookSearchKeywordNextResponse(
            _bookNextRequestUrl);
    setState(() {
      _bookSearchData
          .addAll(additionalBookSearchResponse.data!.bookSearchResult!);
      _bookNextRequestUrl = additionalBookSearchResponse.data!.nextRequestUrl!;
    });
  }
}
