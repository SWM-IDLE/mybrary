import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_response.dart';
import 'package:mybrary/data/model/search/user_search_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/error_page.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/profile/user_profile/user_profile_screen.dart';
import 'package:mybrary/ui/search/components/search_loading.dart';
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
  final ScrollController _searchUserScrollController = ScrollController();
  final ScrollController _tabScrollController = ScrollController();
  final TextEditingController _bookSearchKeywordController =
      TextEditingController();

  final _searchRepository = SearchRepository();
  late Future<BookSearchResponseData> _bookSearchResponse;
  late Future<UserSearchResponseData> _userSearchResponse;

  late String _bookSearchNextUrl;
  late List<BookSearchResult> _bookSearchResultData = [];
  late List<SearchedUsers> _userSearchResultData = [];
  final String _bookSearchKeywordRequestUrl = getApi(API.getBookSearchKeyword);

  late bool _isError = false;
  late bool _isChangedData = false;
  late bool _isScrollLoading = false;
  late bool _isClearButtonVisible = false;

  final _userId = UserState.userId;

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
    _searchUserScrollController.dispose();
    _tabScrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      resizeToAvoidBottomInset: false,
      child: NestedScrollView(
        controller: _tabScrollController,
        physics: const BouncingScrollPhysics(
          parent: AlwaysScrollableScrollPhysics(),
        ),
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
          physics: const BouncingScrollPhysics(
            parent: AlwaysScrollableScrollPhysics(),
          ),
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
                              '${getApi(API.getBookService)}$_bookSearchNextUrl',
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
                      if (bookSearchResponse.bookSearchResult!.isEmpty) ...[
                        Padding(
                          padding: EdgeInsets.only(top: paddingTopHeight * 2),
                          child: const SingleDataError(
                            errorMessage: '검색된 책이 없습니다.',
                          ),
                        ),
                      ] else ...[
                        const SizedBox(height: 8.0),
                        SearchBookListInfo(
                          bookSearchDataList: _bookSearchResultData,
                          scrollController: _searchScrollController,
                          paddingTopHeight: paddingTopHeight,
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
                  List<SearchedUsers> searchedUsers =
                      userSearchResponse.searchedUsers!;

                  return searchedUsersScreen(
                    _isChangedData ? _userSearchResultData : searchedUsers,
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

  Widget searchInputBox() {
    return Padding(
      padding: const EdgeInsets.only(
        top: 0.0,
        left: 18.0,
        bottom: 4.0,
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
              .getUserSearchResponse(
            context: context,
            nickname: _bookSearchKeywordController.text,
          )
              .then((data) {
            setState(() {
              _isChangedData = true;
              if (data.searchedUsers!.isEmpty) {
                _userSearchResultData.clear();
              }
              if (data.searchedUsers!.isNotEmpty) {
                _userSearchResultData = data.searchedUsers!;
              }
            });
          });

          _searchRepository
              .getBookSearchResponse(
                context: context,
                requestUrl:
                    '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
              )
              .then(
                (data) => setState(() {
                  _isError = false;
                  _bookSearchResponse = _searchRepository.getBookSearchResponse(
                    context: context,
                    requestUrl:
                        '$_bookSearchKeywordRequestUrl?keyword=${_bookSearchKeywordController.text}',
                  );
                  _bookSearchResultData = data.bookSearchResult!;
                  _bookSearchNextUrl = data.nextRequestUrl!;
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
    List<String> searchTabs,
    TabController tabController,
  ) {
    return <Widget>[
      SliverOverlapAbsorber(
        handle: NestedScrollView.sliverOverlapAbsorberHandleFor(context),
        sliver: SliverAppBar(
          elevation: 0,
          toolbarHeight: 66.0,
          foregroundColor: commonBlackColor,
          backgroundColor: commonWhiteColor,
          flexibleSpace: FlexibleSpaceBar(
            background: Container(
              color: commonWhiteColor,
            ),
          ),
          title: const Text('검색'),
          titleTextStyle: commonSubTitleStyle.copyWith(
            color: commonBlackColor,
          ),
          centerTitle: true,
          pinned: true,
          forceElevated: innerBoxIsScrolled,
          bottom: PreferredSize(
            preferredSize: const Size.fromHeight(48.0),
            child: searchInputBox(),
          ),
        ),
      ),
      SliverPersistentHeader(
        delegate: _SliverAppBarDelegate(
          searchTabBar(
            tabController,
            searchTabs,
          ),
        ),
        pinned: true,
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
      physics: const BouncingScrollPhysics(),
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
          controller: _searchUserScrollController,
          physics: const BouncingScrollPhysics(
            parent: AlwaysScrollableScrollPhysics(),
          ),
          itemCount: searchedUsers.length,
          itemBuilder: (context, index) {
            SearchedUsers searchedUser = searchedUsers[index];

            return InkWell(
              onTap: () {
                if (_userId != searchedUser.userId) {
                  Navigator.of(context).push(
                    MaterialPageRoute(
                      builder: (_) => UserProfileScreen(
                        userId: searchedUser.userId!,
                        nickname: searchedUser.nickname!,
                      ),
                    ),
                  );
                }
                if (_userId == searchedUser.userId!) {
                  Navigator.of(context).pushAndRemoveUntil(
                    MaterialPageRoute(
                      builder: (_) => const RootTab(
                        tapIndex: 3,
                      ),
                    ),
                    (route) => false,
                  );
                }
              },
              child: SearchUserLayout(
                children: [
                  SearchUserInfo(
                    nickname: searchedUser.nickname!,
                    profileImageUrl: searchedUser.profileImageUrl!,
                  ),
                ],
              ),
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

class _SliverAppBarDelegate extends SliverPersistentHeaderDelegate {
  _SliverAppBarDelegate(this._tabBar);

  final TabBar _tabBar;

  @override
  double get minExtent => _tabBar.preferredSize.height;
  @override
  double get maxExtent => _tabBar.preferredSize.height;

  @override
  Widget build(
      BuildContext context, double shrinkOffset, bool overlapsContent) {
    return Container(
      height: _tabBar.preferredSize.height,
      color: commonWhiteColor,
      child: _tabBar,
    );
  }

  @override
  bool shouldRebuild(_SliverAppBarDelegate oldDelegate) {
    return false;
  }
}
