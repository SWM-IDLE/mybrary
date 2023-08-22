import 'dart:io';

import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/enum.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/mybook/mybook_detail/mybook_detail_screen.dart';
import 'package:mybrary/ui/mybook/mybook_list/components/book_list.dart';
import 'package:mybrary/utils/logics/book_utils.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class MyBookListScreen extends StatefulWidget {
  final String? userId;
  final String bookListTitle;
  final String order;
  final String readStatus;

  const MyBookListScreen({
    this.userId,
    required this.bookListTitle,
    required this.order,
    required this.readStatus,
    super.key,
  });

  @override
  State<MyBookListScreen> createState() => _MyBookListScreenState();
}

class _MyBookListScreenState extends State<MyBookListScreen> {
  final _bookRepository = BookRepository();
  late Future<List<MyBooksResponseData>> _bookList;
  late SortType _sortType;
  late String _sortTitle = '전체';
  late String _order = 'all';

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _bookList = _bookRepository.getMyBooks(
      context: context,
      userId: widget.userId ?? _userId,
      order: widget.order,
      readStatus: widget.readStatus,
    );
    _sortType = SortType.all;
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      child: FutureBuilder<List<MyBooksResponseData>>(
        future: _bookList,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                commonSliverAppBar(appBarTitle: widget.bookListTitle),
                const SliverToBoxAdapter(
                  child: DataError(
                    errorMessage: '등록된 도서를 불러오는데 실패했습니다.',
                  ),
                ),
              ],
            );
          }

          if (snapshot.hasData) {
            List<MyBooksResponseData> bookList = snapshot.data!;

            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                commonSliverAppBar(appBarTitle: widget.bookListTitle),
                if (bookList.isEmpty)
                  const SliverToBoxAdapter(
                    child: DataError(
                      errorMessage: '등록된 도서가 없습니다.',
                    ),
                  ),
                if (bookList.isNotEmpty)
                  SliverPadding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16.0,
                      vertical: 8.0,
                    ),
                    sliver: SliverToBoxAdapter(
                      child: InkWell(
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
                    ),
                  ),
                if (bookList.isNotEmpty)
                  BookList(
                    bookList: bookList,
                    readStatus: widget.readStatus,
                    onTapMyBookDetail: _refreshMyBookScreen,
                  ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  void _refreshMyBookScreen(
    int myBookId,
    String readStatus,
  ) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => MyBookDetailScreen(
          myBookId: myBookId,
          userId: widget.userId,
        ),
      ),
    ).then(
      (value) => setState(() {
        _bookList = _bookRepository.getMyBooks(
          context: context,
          userId: widget.userId ?? _userId,
          order: _order,
          readStatus: readStatus,
        );
      }),
    );
  }

  void _onTapSortItem(SortType sortType) {
    setState(() {
      _sortType = sortType;
    });
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
                      _bookList = _bookRepository.getMyBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'title',
                        readStatus: widget.readStatus,
                      );
                      _sortTitle = '제목순';
                      _order = 'title';
                      break;
                    case SortType.registration:
                      _bookList = _bookRepository.getMyBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'registration',
                        readStatus: widget.readStatus,
                      );
                      _sortTitle = '등록순';
                      _order = 'registration';
                      break;
                    case SortType.publication:
                      _bookList = _bookRepository.getMyBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'publication',
                        readStatus: widget.readStatus,
                      );
                      _sortTitle = '발행일순';
                      _order = 'publication';
                      break;
                    default:
                      _bookList = _bookRepository.getMyBooks(
                        context: context,
                        userId: widget.userId ?? _userId,
                        order: 'all',
                        readStatus: widget.readStatus,
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
}
