import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/book_detail_divider.dart';
import 'package:mybrary/ui/common/components/bottom_button.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/mybook/interest_book_list/interest_book_list_screen.dart';
import 'package:mybrary/ui/search/search_detail/components/book_contents.dart';
import 'package:mybrary/ui/search/search_detail/components/book_description.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_header.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_info.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_provider.dart';
import 'package:mybrary/ui/search/search_detail/components/book_details.dart';

class SearchDetailScreen extends StatefulWidget {
  final String isbn13;
  const SearchDetailScreen({
    required this.isbn13,
    super.key,
  });

  @override
  State<SearchDetailScreen> createState() => _SearchDetailScreenState();
}

class _SearchDetailScreenState extends State<SearchDetailScreen> {
  final _searchRepository = SearchRepository();
  final _bookRepository = BookRepository();

  final ScrollController _bookDetailScrollController = ScrollController();
  late Future<BookSearchDetailResponseData> _bookSearchDetailResponse;

  late String _bookTitle = '';
  late int _newInterestCount = 0;
  late bool _isScrollingDown = true;
  late bool _isOnTapInterestBook = false;
  late bool _isOverflowBookDetailHeader = false;
  final GlobalKey _bookDetailHeaderKey = GlobalKey();

  @override
  void initState() {
    super.initState();

    _bookSearchDetailResponse =
        _searchRepository.getBookSearchDetailAndSaveBookResponse(
      userId: 'testId',
      isbn13: widget.isbn13,
    );

    _searchRepository
        .getBookSearchDetailAndSaveBookResponse(
      userId: 'testId',
      isbn13: widget.isbn13,
    )
        .then(
      (response) {
        setState(() {
          _isOnTapInterestBook = response.interested!;
          _newInterestCount = response.interestCount!;
        });
      },
    );

    _bookDetailScrollController.addListener(_bookDetailScrollListener);
  }

  void _bookDetailScrollListener() {
    double? position;
    ScrollPosition scrollPosition = _bookDetailScrollController.position;

    if (_bookDetailHeaderKey.currentContext != null) {
      position = _bookDetailHeaderKey.currentContext!.size!.height;
    }

    if (scrollPosition.pixels > position!) {
      setState(() {
        _isOverflowBookDetailHeader = true;
      });
    } else {
      setState(() {
        _isOverflowBookDetailHeader = false;
      });
    }

    if (scrollPosition.pixels ==
            _bookDetailScrollController.position.maxScrollExtent ||
        scrollPosition.userScrollDirection == ScrollDirection.forward) {
      setState(() {
        _isScrollingDown = true;
      });
    } else if (scrollPosition.userScrollDirection == ScrollDirection.reverse) {
      setState(() {
        _isScrollingDown = false;
      });
    }
  }

  @override
  void dispose() {
    _bookDetailScrollController.removeListener(_bookDetailScrollListener);
    _bookDetailScrollController.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: _isOverflowBookDetailHeader ? _bookTitle : '',
      appBarActions: [
        IconButton(
          onPressed: () {
            Navigator.pushAndRemoveUntil(context, MaterialPageRoute(
              builder: (context) {
                return const RootTab();
              },
            ), (route) => false);
          },
          icon: SvgPicture.asset(
            'assets/svg/icon/home.svg',
          ),
        ),
      ],
      child: SafeArea(
        bottom: false,
        child: FutureBuilder<BookSearchDetailResponseData>(
          future: _bookSearchDetailResponse,
          builder: (context, snapshot) {
            if (snapshot.hasError) {
              return const SingleDataError(
                errorMessage: '도서 정보를 불러오는데 실패했습니다.',
              );
            }

            if (snapshot.hasData) {
              final bookSearchDetail = snapshot.data!;
              bool interested = bookSearchDetail.interested!;
              int interestCount = bookSearchDetail.interestCount!;

              _bookTitle = bookSearchDetail.title!;

              return Stack(
                children: [
                  SingleChildScrollView(
                    controller: _bookDetailScrollController,
                    physics: const BouncingScrollPhysics(),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        BookDetailHeader(
                          key: _bookDetailHeaderKey,
                          onTapInterestBook: () async {
                            final result = await _bookRepository
                                .createOrDeleteInterestBook(
                              userId: 'testId',
                              isbn13: bookSearchDetail.isbn13!,
                            );

                            setState(() {
                              _isOnTapInterestBook = result.interested!;

                              _isInterestBook(
                                interested,
                                interestCount,
                                context,
                              );
                            });
                          },
                          interested: interested,
                          isOnTapHeart: _isOnTapInterestBook,
                          thumbnail: bookSearchDetail.thumbnail!,
                          title: bookSearchDetail.title!,
                          authors: bookSearchDetail.authors!,
                          interestCount: interestCount,
                          newInterestCount: _newInterestCount,
                          readCount: bookSearchDetail.readCount!,
                          holderCount: bookSearchDetail.holderCount!,
                        ),
                        const BookDetailDivider(),
                        BookDetailInfo(
                          publicationDate: bookSearchDetail.publicationDate!,
                          category: bookSearchDetail.category!,
                          pages: bookSearchDetail.pages!,
                          publisher: bookSearchDetail.publisher!,
                          starRating: bookSearchDetail.starRating!,
                          link: bookSearchDetail.link!,
                          aladinStarRating: bookSearchDetail.aladinStarRating!,
                        ),
                        const BookDetailDivider(),
                        bookDetailExpansion(
                          expansionTitle: '책 소개',
                          initiallyExpanded: true,
                          children: BookDescription(
                            subTitle: bookSearchDetail.subTitle!,
                            description: bookSearchDetail.description!,
                          ),
                        ),
                        const BookDetailDivider(),
                        bookDetailExpansion(
                          expansionTitle: '목차',
                          children: BookContents(
                            toc: bookSearchDetail.toc!,
                          ),
                        ),
                        const BookDetailDivider(),
                        BookDetails(
                          isbn10: bookSearchDetail.isbn10!,
                          isbn13: bookSearchDetail.isbn13!,
                          weight: bookSearchDetail.weight!,
                          sizeDepth: bookSearchDetail.sizeDepth!,
                          sizeHeight: bookSearchDetail.sizeHeight!,
                          sizeWidth: bookSearchDetail.sizeWidth!,
                          translators: bookSearchDetail.translators!,
                        ),
                        const BookDetailDivider(),
                        BookDetailProvider(
                          link: bookSearchDetail.link!,
                        ),
                        const SizedBox(height: 80.0),
                      ],
                    ),
                  ),
                  BottomButton(
                    isScrollingDown: _isScrollingDown,
                    buttonText: '마이북에 담기',
                    onTap: () => _onTapSaveMyBook(bookSearchDetail.isbn13!),
                  ),
                ],
              );
            }
            return const CircularLoading();
          },
        ),
      ),
    );
  }

  void _isInterestBook(
    bool interested,
    int interestCount,
    BuildContext context,
  ) {
    if (!interested && _isOnTapInterestBook) {
      _newInterestCount = interestCount + 1;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
        snackBarAction: _moveNextToInterestBookListScreen(),
      );
    } else if (interested && _isOnTapInterestBook == false) {
      _newInterestCount = interestCount - 1;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서가 삭제되었습니다.',
      );
    } else if (interested && _isOnTapInterestBook) {
      _newInterestCount = interestCount;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
        snackBarAction: _moveNextToInterestBookListScreen(),
      );
    } else {
      _newInterestCount = interestCount;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서가 삭제되었습니다.',
      );
    }
  }

  ExpansionTile bookDetailExpansion({
    required String expansionTitle,
    required Widget children,
    bool? initiallyExpanded,
  }) {
    return ExpansionTile(
      title: Text(
        expansionTitle,
        style: commonSubBoldStyle,
      ),
      tilePadding: const EdgeInsets.symmetric(horizontal: 20.0),
      childrenPadding: const EdgeInsets.symmetric(
        horizontal: 20.0,
        vertical: 6.0,
      ),
      initiallyExpanded: initiallyExpanded ?? false,
      expandedAlignment: Alignment.centerLeft,
      shape: const Border(),
      iconColor: grey777777,
      collapsedIconColor: grey777777,
      children: [
        children,
      ],
    );
  }

  void _showInterestBookMessage({
    required BuildContext context,
    required String snackBarText,
    Widget? snackBarAction,
  }) {
    if (context.mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          padding: EdgeInsets.symmetric(
            horizontal: 24.0,
            vertical: Platform.isAndroid ? 22.0 : 16.0,
          ),
          content: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                snackBarText,
                style: commonSnackBarMessageStyle.copyWith(fontSize: 14.0),
              ),
              snackBarAction ?? const SizedBox(),
            ],
          ),
          duration: Duration(
            seconds: snackBarAction == null ? 1 : 2,
          ),
        ),
      );
    }
  }

  Widget _moveNextToInterestBookListScreen() {
    return InkWell(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => const InterestBookListScreen(),
          ),
        );
        ScaffoldMessenger.of(context).hideCurrentSnackBar();
      },
      child: const Text(
        '관심북으로 이동',
        style: commonSnackBarButtonStyle,
      ),
    );
  }

  void _onTapSaveMyBook(String isbn13) async {
    await _bookRepository.createMyBook(
      userId: 'testId',
      isbn13: isbn13,
    );

    if (!mounted) return;
    showDialog(
      context: context,
      barrierColor: commonBlackColor.withOpacity(0.2),
      barrierDismissible: false,
      builder: (BuildContext context) {
        return Center(
          child: Container(
            width: MediaQuery.of(context).size.width * 0.5,
            height: 36.0,
            decoration: BoxDecoration(
              color: grey262626,
              borderRadius: BorderRadius.circular(10.0),
            ),
            child: Center(
              child: DefaultTextStyle(
                style: commonSubRegularStyle.copyWith(
                  color: commonWhiteColor,
                ),
                child: const Text('마이북에 담겼습니다.'),
              ),
            ),
          ),
        );
      },
    );

    Future.delayed(const Duration(seconds: 1), () {
      Navigator.of(context).pop();
    });
  }
}
