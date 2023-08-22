import 'dart:io' show Platform;

import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/enum.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/mybook/interest_book_list/components/interest_book_list.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class InterestBookListScreen extends StatefulWidget {
  final String? userId;

  const InterestBookListScreen({
    this.userId,
    super.key,
  });

  @override
  State<InterestBookListScreen> createState() => _InterestBookListScreenState();
}

class _InterestBookListScreenState extends State<InterestBookListScreen> {
  final _bookRepository = BookRepository();
  late Future<List<BookListResponseData>> _bookList;
  late SortType _sortType;
  late String _sortTitle = '전체';
  late String _order = 'all';

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _bookList = _bookRepository.getInterestBooks(
      context: context,
      userId: widget.userId ?? _userId,
    );
    _sortType = SortType.all;
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      child: FutureBuilder<List<BookListResponseData>>(
          future: _bookList,
          builder: (context, snapshot) {
            if (snapshot.hasError) {
              return CustomScrollView(
                physics: const BouncingScrollPhysics(
                  parent: AlwaysScrollableScrollPhysics(),
                ),
                slivers: [
                  _bookListAppBar(appBarTitle: '관심북'),
                  const SliverToBoxAdapter(
                    child: DataError(
                      errorMessage: '등록된 도서를 불러오는데 실패했습니다.',
                    ),
                  )
                ],
              );
            }

            if (snapshot.hasData) {
              List<BookListResponseData> bookList = snapshot.data!;

              return CustomScrollView(
                physics: const BouncingScrollPhysics(
                  parent: AlwaysScrollableScrollPhysics(),
                ),
                slivers: [
                  _bookListAppBar(appBarTitle: '관심북'),
                  if (bookList.isEmpty)
                    const SliverToBoxAdapter(
                      child: DataError(
                        errorMessage: '등록된 도서가 없습니다.',
                      ),
                    ),
                  if (bookList.isNotEmpty) _sortTapColumn(context, bookList),
                  if (bookList.isNotEmpty)
                    InterestBookList(
                      bookList: bookList,
                      onTap: _refreshMyInterestBooksScreen,
                    ),
                ],
              );
            }
            return const CircularLoading();
          }),
    );
  }

  SliverPadding _sortTapColumn(
      BuildContext context, List<BookListResponseData> bookList) {
    return SliverPadding(
      padding: const EdgeInsets.symmetric(
        horizontal: 16.0,
        vertical: 8.0,
      ),
      sliver: SliverToBoxAdapter(
        child: Row(
          children: [
            InkWell(
              onTap: () {
                _showSortBottomSheet(context);
              },
              child: Row(
                children: [
                  Text(
                    '$_sortTitle ${bookList.length}권',
                    style: myBookSortTextStyle,
                  ),
                  const SizedBox(width: 4.0),
                  const Icon(
                    Icons.arrow_drop_down_outlined,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<dynamic> _showSortBottomSheet(BuildContext context) {
    return showMenuBottomSheet(
      context,
      StatefulBuilder(
        builder: (BuildContext context, StateSetter bottomState) {
          return SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Padding(
                  padding: EdgeInsets.symmetric(
                    vertical: 16.0,
                  ),
                  child: Text(
                    '정렬',
                    style: commonSubBoldStyle,
                  ),
                ),
                _sortItemRow(
                  sortItemLeft: _sortItem(
                    onTap: () {
                      bottomState(() {
                        _onTapSortItem(
                          SortType.all,
                        );
                      });
                    },
                    sortTitle: '전체',
                    sortItemType: SortType.all,
                  ),
                  sortItemRight: _sortItem(
                    onTap: () {
                      bottomState(() {
                        _onTapSortItem(
                          SortType.title,
                        );
                      });
                    },
                    sortTitle: '제목순',
                    sortItemType: SortType.title,
                  ),
                ),
                _sortItemRow(
                  sortItemLeft: _sortItem(
                    onTap: () {
                      bottomState(() {
                        _onTapSortItem(
                          SortType.registration,
                        );
                      });
                    },
                    sortTitle: '등록순',
                    sortItemType: SortType.registration,
                  ),
                  sortItemRight: _sortItem(
                    onTap: () {
                      bottomState(() {
                        _onTapSortItem(
                          SortType.publication,
                        );
                      });
                    },
                    sortTitle: '발행일순',
                    sortItemType: SortType.publication,
                  ),
                ),
                _sortButton(context),
              ],
            ),
          );
        },
      ),
    );
  }

  Padding _sortButton(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(
        top: 16.0,
        bottom: Platform.isIOS ? 32.0 : 0.0,
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            flex: 1,
            child: InkWell(
              onTap: () {
                Navigator.pop(context);
              },
              child: Container(
                color: greyDDDDDD,
                padding: const EdgeInsets.symmetric(
                  vertical: 12.0,
                ),
                child: const Text(
                  '닫기',
                  style: commonSubMediumStyle,
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ),
          Expanded(
            flex: 2,
            child: InkWell(
              onTap: () {
                setState(() {
                  switch (_sortType) {
                    case SortType.title:
                      _bookList = _bookRepository.getInterestBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'title',
                      );
                      _sortTitle = '제목순';
                      _order = 'title';
                      break;
                    case SortType.registration:
                      _bookList = _bookRepository.getInterestBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'registration',
                      );
                      _sortTitle = '등록순';
                      _order = 'registration';
                      break;
                    case SortType.publication:
                      _bookList = _bookRepository.getInterestBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'publication',
                      );
                      _sortTitle = '발행일순';
                      _order = 'publication';
                      break;
                    default:
                      _bookList = _bookRepository.getInterestBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                      );
                      _sortTitle = '전체';
                      _order = 'all';
                  }
                  Navigator.pop(context);
                });
              },
              child: Container(
                color: primaryColor,
                padding: const EdgeInsets.symmetric(
                  vertical: 12.0,
                ),
                child: Text(
                  '적용하기',
                  style: commonSubMediumStyle.copyWith(
                    color: commonWhiteColor,
                  ),
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _sortItemRow({
    required Widget sortItemLeft,
    required Widget sortItemRight,
  }) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          sortItemLeft,
          const SizedBox(width: 16.0),
          sortItemRight,
        ],
      ),
    );
  }

  Widget _sortItem({
    required void Function() onTap,
    required String sortTitle,
    required SortType sortItemType,
  }) {
    return InkWell(
      onTap: onTap,
      child: Container(
        width: 160,
        padding: const EdgeInsets.symmetric(
          vertical: 14.0,
        ),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          border: Border.all(
            color: _sortType == sortItemType ? primaryColor : greyACACAC,
          ),
          borderRadius: BorderRadius.circular(5.0),
        ),
        child: Text(
          sortTitle,
          style: commonSubRegularStyle.copyWith(
            color: _sortType == sortItemType ? grey262626 : grey999999,
            fontWeight:
                _sortType == sortItemType ? FontWeight.w500 : FontWeight.w400,
          ),
        ),
      ),
    );
  }

  SliverAppBar _bookListAppBar({
    required String appBarTitle,
    List<Widget>? appBarActions,
  }) {
    return SliverAppBar(
      elevation: 0,
      title: Text(appBarTitle),
      titleTextStyle: appBarTitleStyle.copyWith(
        fontSize: 16.0,
      ),
      pinned: true,
      centerTitle: true,
      backgroundColor: commonWhiteColor,
      foregroundColor: commonBlackColor,
      actions: appBarActions,
    );
  }

  void _onTapSortItem(SortType sortType) {
    setState(() {
      _sortType = sortType;
    });
  }

  void _refreshMyInterestBooksScreen(String isbn13) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => SearchDetailScreen(
          isbn13: isbn13,
        ),
      ),
    ).then(
      (value) => setState(() {
        _bookList = _bookRepository.getInterestBooks(
          context: context,
          userId: widget.userId ?? _userId,
          order: _order,
        );
      }),
    );
  }
}
