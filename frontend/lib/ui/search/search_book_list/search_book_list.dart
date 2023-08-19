import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/model/search/user_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/error_page.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/search/components/search_loading.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_header.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_book_list_info.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_user_info.dart';
import 'package:mybrary/ui/search/search_book_list/components/search_user_layout.dart';

class SearchBookList extends StatefulWidget {
  final String searchKeyword;

  const SearchBookList({
    required this.searchKeyword,
    super.key,
  });

  @override
  State<SearchBookList> createState() => _SearchBookListState();
}

class _SearchBookListState extends State<SearchBookList>
    with TickerProviderStateMixin {
  late final List<String> _searchTabs = ['도서 전체', '사용자'];
  late final TabController _tabController = TabController(
    length: _searchTabs.length,
    vsync: this,
  );

  final ScrollController _searchScrollController = ScrollController();
  final ScrollController _tabScrollController = ScrollController();
  final TextEditingController _bookSearchKeywordController =
      TextEditingController();

  final _searchRepository = SearchRepository();
  late Future<BookSearchResponseData> _bookSearchResponse;
  late Future<UserSearchResponseData> _userSearchResponse;

  late String _bookSearchNextUrl;
  late List<BookSearchResult> _bookSearchResultData = [];
  final String _bookSearchKeywordRequestUrl =
      getBookServiceApi(API.getBookSearchKeyword);

  late bool _isError = false;
  late bool _isScrollLoading = false;
  late bool _isClearButtonVisible = false;

  @override
  void initState() {
    super.initState();

    _bookSearchKeywordController.text = widget.searchKeyword;
    _isClearButtonVisible = true;
    _bookSearchResponse = _searchRepository.getBookSearchResponse(
      context: context,
      requestUrl:
          '$_bookSearchKeywordRequestUrl?keyword=${widget.searchKeyword}',
    );
    _userSearchResponse = _searchRepository.getUserSearchResponse(
      context: context,
      nickname: widget.searchKeyword,
    );
    _searchScrollController.addListener(_infiniteScrollUpdateBookList);
  }

  void _infiniteScrollUpdateBookList() {
    setState(() {
      if (_searchScrollController.position.pixels >
          _searchScrollController.position.maxScrollExtent * 0.85) {
        _isScrollLoading = true;
      } else {
        _isScrollLoading = false;
      }
    });
  }

  @override
  void dispose() {
    _bookSearchKeywordController.dispose();
    _searchScrollController.dispose();
    _tabScrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      child: NestedScrollView(
        controller: _tabScrollController,
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          return searchPageSliverBuilder(
            context,
            innerBoxIsScrolled,
            _searchTabs,
            _tabController,
          );
        },
        body: TabBarView(
          controller: _tabController,
          children: [
            FutureBuilder<BookSearchResponseData>(
              future: _bookSearchResponse,
              builder: (context, snapshot) {
                if (_isError || snapshot.hasError) {
                  return SubPageLayout(
                    appBarTitle: '검색',
                    child: Column(
                      children: [
                        searchInputBox(),
                        const ErrorPage(
                          errorMessage: '검색 결과를 불러오는데 실패했습니다.',
                        ),
                      ],
                    ),
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
                          context: context,
                          requestUrl:
                              '${getBookServiceApi(API.getBookService)}$_bookSearchNextUrl',
                        )
                        .then((value) => setState(() {
                              _bookSearchResultData
                                  .addAll(value.bookSearchResult!);
                              _bookSearchNextUrl = value.nextRequestUrl!;
                            }));

                    _bookSearchNextUrl = "";
                    _isScrollLoading = false;
                  }
                  return Column(
                    children: [
                      searchInputBox(),
                      if (_bookSearchResultData.isEmpty) ...[
                        const SingleDataError(
                          errorMessage: '검색된 책이 없습니다.',
                        ),
                      ] else ...[
                        SearchBookListHeader(
                          keyword: _bookSearchKeywordController.text,
                          bookSearchDataList: _bookSearchResultData,
                        ),
                        SearchBookListInfo(
                          bookSearchDataList: _bookSearchResultData,
                          scrollController: _searchScrollController,
                        ),
                      ],
                    ],
                  );
                }
                return const SingleDataError(
                  errorMessage: '검색 결과를 불러오는데 실패했습니다.',
                );
              },
            ),
            FutureBuilder<UserSearchResponseData>(
              future: _userSearchResponse,
              builder: (context, snapshot) {
                if (snapshot.hasError) {
                  return SubPageLayout(
                    appBarTitle: '검색',
                    child: Column(
                      children: [
                        searchInputBox(),
                        const ErrorPage(
                          errorMessage: '검색 결과를 불러오는데 실패했습니다.',
                        ),
                      ],
                    ),
                  );
                }

                if (!snapshot.hasData) {
                  return const SearchLoading();
                }

                if (snapshot.hasData) {
                  UserSearchResponseData userSearchResponse = snapshot.data!;
                  return searchedUsersScreen(
                    userSearchResponse.searchedUsers!,
                  );
                }
                return const SingleDataError(
                  errorMessage: '검색 결과를 불러오는데 실패했습니다.',
                );
              },
            ),
          ],
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
                context: context,
                requestUrl:
                    '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
              )
              .then(
                (value) => setState(() {
                  _isError = false;
                  _bookSearchResponse = _searchRepository.getBookSearchResponse(
                    context: context,
                    requestUrl:
                        '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
                  );
                  _bookSearchResultData = value.bookSearchResult!;
                  _bookSearchNextUrl = value.nextRequestUrl!;
                  if (_searchScrollController.hasClients) {
                    _searchScrollController.animateTo(
                      0,
                      duration: const Duration(microseconds: 500),
                      curve: Curves.easeInOut,
                    );
                  }
                }),
              )
              .onError((error, stackTrace) {
            setState(() {
              _isError = true;
            });
          });
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

  List<Widget> searchPageSliverBuilder(
    BuildContext context,
    bool innerBoxIsScrolled,
    List<String> followerTabs,
    TabController tabController,
  ) {
    return <Widget>[
      SliverOverlapAbsorber(
        handle: NestedScrollView.sliverOverlapAbsorberHandleFor(context),
        sliver: SliverAppBar(
          elevation: 0,
          foregroundColor: commonBlackColor,
          backgroundColor: commonWhiteColor,
          // expandedHeight: 120,
          flexibleSpace: FlexibleSpaceBar(
            background: Container(
              color: commonWhiteColor,
            ),
          ),
          title: const Text('검색'),
          titleTextStyle: appBarTitleStyle.copyWith(
            fontSize: 16.0,
          ),
          centerTitle: true,
          pinned: true,
          expandedHeight: 104.01,
          forceElevated: innerBoxIsScrolled,
          bottom: searchTabBar(
            tabController,
            followerTabs,
          ),
        ),
      ),
    ];
  }

  TabBar searchTabBar(
    TabController tabController,
    List<String> searchTabs,
  ) {
    return TabBar(
      controller: tabController,
      indicatorColor: grey262626,
      labelColor: grey262626,
      labelStyle: commonButtonTextStyle,
      unselectedLabelColor: greyACACAC,
      unselectedLabelStyle: commonButtonTextStyle.copyWith(
        fontWeight: FontWeight.w400,
      ),
      tabs: searchTabs.map((String name) => Tab(text: name)).toList(),
    );
  }

  Widget searchedUsersScreen(List<SearchedUsers> searchedUsers) {
    if (searchedUsers.isNotEmpty) {
      return Padding(
        padding: EdgeInsets.only(top: paddingTopHeight),
        child: ListView.builder(
          physics: const BouncingScrollPhysics(),
          itemCount: searchedUsers.length,
          itemBuilder: (context, index) {
            SearchedUsers searchedUser = searchedUsers[index];

            return SearchUserLayout(
              children: [
                SearchUserInfo(
                  nickname: searchedUser.nickname!,
                  profileImageUrl: searchedUser.profileImageUrl!,
                ),
              ],
            );
          },
        ),
      );
    } else {
      return const SingleDataError(
        errorMessage: '검색된 사용자가 없습니다.',
      );
    }
  }
}
