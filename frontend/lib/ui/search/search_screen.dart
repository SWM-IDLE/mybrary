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
  late Future<BookSearchResponse> bookSearchItems;
  late List<BookSearchData> bookSearchData = [];
  late Future<BookSearchResponse> bookNextSearchResponse;
  late bool isSearching = false;
  late bool isMaxScrollExtent = false;

  final ScrollController scrollController = ScrollController();
  final TextEditingController bookSearchController = TextEditingController();

  void getBookSearchPopularKeywordData(bool isBinding) {
    final popularKeyword = bookSearchController.text;
    setState(() {
      if (popularKeyword != "") {
        isBinding = true;
        bookSearchItems =
            RemoteDataSource.getBookSearchKeywordResponse(popularKeyword);
        isSearching = true;
      }
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    scrollController.addListener(() {
      ScrollPosition scrollPosition = scrollController.position;
      setState(() {
        if (scrollPosition.pixels == scrollPosition.maxScrollExtent) {
          isMaxScrollExtent = true;
        }
      });
    });
  }

  @override
  void dispose() {
    bookSearchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final searchInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide.none,
      borderRadius: BorderRadius.circular(10.0),
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
                      controller: bookSearchController,
                      cursorColor: Colors.green,
                      onSubmitted: (value) {
                        setState(() {
                          bookSearchItems =
                              RemoteDataSource.getBookSearchKeywordResponse(
                                  value);
                          isSearching = true;
                        });
                      },
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.symmetric(vertical: 12.0),
                        hintText: '책, 저자, 회원을 검색해보세요.',
                        hintStyle: TextStyle(
                          fontSize: 14.0,
                        ),
                        filled: true,
                        fillColor: Colors.grey.withOpacity(0.2),
                        focusedBorder: searchInputBorderStyle,
                        enabledBorder: searchInputBorderStyle,
                        focusColor: Colors.grey,
                        prefixIcon: Icon(Icons.search, color: GREY_COLOR),
                        suffixIcon: IconButton(
                          onPressed: () {
                            setState(() {
                              bookSearchController.clear();
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
                if (!isSearching)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 8.0),
                    child: IconButton(
                      onPressed: () {},
                      icon: Icon(Icons.camera_alt_outlined),
                    ),
                  )
                else
                  TextButton(
                    onPressed: () {
                      setState(() {
                        bookSearchController.clear();
                        bookSearchData.clear();
                        isSearching = false;
                      });
                    },
                    child: Text(
                      '취소',
                      style: TextStyle(
                        color: LESS_BLACK_COLOR,
                        fontSize: 14.0,
                      ),
                    ),
                  )
              ],
            ),
            SizedBox(
              height: 8.0,
            ),
            if (!isSearching)
              SearchPopularKeyword(
                bookSearchController: bookSearchController,
                onBookSearchBinding: getBookSearchPopularKeywordData,
              )
            else
              FutureBuilder<BookSearchResponse>(
                future: bookSearchItems,
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
                    if (bookSearchData.isEmpty) {
                      bookSearchData
                          .addAll(bookSearchResponse.data!.bookSearchResult!);
                    }

                    // if (isMaxScrollExtent &&
                    //     bookSearchResponse.data!.nextRequestUrl != "") {
                    //   bookNextSearchResponse =
                    //       RemoteDataSource.getBookSearchKeywordResponse("",
                    //           nextRequestUrl:
                    //               bookSearchResponse.data!.nextRequestUrl!);
                    //   bookNextSearchResponse.then((value) {
                    //     bookSearchData.addAll(value.data!.bookSearchResult!);
                    //   }).catchError((e) {
                    //     log('ERROR: nextRequestUrl 요청에 대한 오류입니다. $e');
                    //   });
                    // }

                    return SearchBookList(
                      searchBookList: bookSearchData,
                      scrollController: scrollController,
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
}
