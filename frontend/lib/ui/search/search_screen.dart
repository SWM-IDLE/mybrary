import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';
import 'package:mybrary/data/model/search/book_search_data.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/ui/search/components/search_book_list.dart';
import 'package:mybrary/ui/search/components/search_header.dart';
import 'package:mybrary/ui/search/components/search_loading.dart';
import 'package:mybrary/ui/search/components/search_not_found.dart';
import 'package:mybrary/ui/search/components/search_popular_keyword.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  late Future<BookSearchResponse> _bookSearchResponse;
  late final List<BookSearchData> _bookSearchData = [];
  late BookSearchResponse bookSearchNextResponse;
  late String _bookNextRequestUrl;

  bool _isSearching = false;

  final ScrollController _scrollController = ScrollController();
  final TextEditingController _bookSearchController = TextEditingController();

  @override
  void setState(VoidCallback fn) {
    super.setState(fn);

    // 스크롤 맨 하단에 닿기 바로 이전에 실행 (max x 0.85)
    _scrollController.addListener(() async {
      ScrollPosition scrollPosition = _scrollController.position;
      if (_scrollController.offset > scrollPosition.maxScrollExtent * 0.85 &&
          !scrollPosition.outOfRange) {
        if (_bookNextRequestUrl != "") {
          // nextUrl 이 있을 때 추가 데이터 호출
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
    return Scaffold(
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SearchHeader(
              isSearching: _isSearching,
              bookSearchController: _bookSearchController,
              onSubmittedSearchKeyword: _onSubmittedSearchKeyword,
              onTextClearPressed: _onTextClearPressed,
              onSearchCancelPressed: _onSearchCancelPressed,
            ),
            const SizedBox(
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
                    return const SearchNotFound();
                  }

                  // 서버 요청을 기다리는 경우
                  if (!snapshot.hasData) {
                    return const SearchLoading();
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

                  return const SearchNotFound();
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
        _bookSearchResponse = RemoteDataSource.getBookSearchKeywordResponse(
            BookSearchRequestType.searchKeyword, popularKeyword);
        _isSearching = true;
      }
    });
  }

  void _onSubmittedSearchKeyword(value) {
    setState(() {
      _bookSearchController.text = value;
      _bookSearchResponse = _fetchBookSearchResponse();
      _isSearching = true;
    });
  }

  void _onTextClearPressed() {
    setState(() {
      _bookSearchController.clear();
    });
  }

  void _onSearchCancelPressed() {
    setState(() {
      _bookSearchController.clear();
      _bookSearchData.clear();
      _isSearching = false;
    });
  }

  Future<BookSearchResponse> _fetchBookSearchResponse() async {
    BookSearchResponse bookSearchResponse =
        await RemoteDataSource.getBookSearchKeywordResponse(
            BookSearchRequestType.searchKeyword, _bookSearchController.text);

    return bookSearchResponse;
  }

  Future<void> _fetchNextBookSearchResponse() async {
    BookSearchResponse additionalBookSearchResponse =
        await RemoteDataSource.getBookSearchKeywordResponse(
            BookSearchRequestType.searchNextUrl, _bookNextRequestUrl);

    setState(() {
      _bookSearchData
          .addAll(additionalBookSearchResponse.data!.bookSearchResult!);
      _bookNextRequestUrl = additionalBookSearchResponse.data!.nextRequestUrl!;
    });
  }
}
