import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_completed_status_response.dart';
import 'package:mybrary/data/model/search/book_interest_status_response.dart';
import 'package:mybrary/data/model/search/book_registered_status_response.dart';
import 'package:mybrary/data/model/search/book_search_common_response.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/book_detail_divider.dart';
import 'package:mybrary/ui/common/components/bottom_button.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/search/search_detail/components/book_contents.dart';
import 'package:mybrary/ui/search/search_detail/components/book_description.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_header.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_info.dart';
import 'package:mybrary/ui/search/search_detail/components/book_detail_provider.dart';
import 'package:mybrary/ui/search/search_detail/components/book_details.dart';
import 'package:mybrary/ui/search/search_detail_review/search_detail_review_screen.dart';

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

  late Future<BookInterestStatusResponseData> _bookInterestStatusResponseData;
  late Future<BookRegisteredStatusResponseData>
      _bookRegisteredStatusResponseData;
  late Future<BookCompletedStatusResponseData> _bookCompletedStatusResponseData;

  late String _bookTitle = '';
  late bool _isScrollingDown = true;
  late bool _registeredMyBook = false;
  late bool _isOverflowBookDetailHeader = false;

  final GlobalKey _bookDetailHeaderKey = GlobalKey();
  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _bookSearchDetailResponse =
        _searchRepository.getBookSearchDetailAndSaveBookResponse(
      context: context,
      userId: _userId,
      isbn13: widget.isbn13,
    );

    _bookInterestStatusResponseData =
        _searchRepository.getBookInterestStatusResponse(
      context: context,
      isbn13: widget.isbn13,
    );
    _bookRegisteredStatusResponseData =
        _searchRepository.getBookRegisteredStatusResponse(
      context: context,
      isbn13: widget.isbn13,
    );
    _bookCompletedStatusResponseData =
        _searchRepository.getBookCompletedStatusResponse(
      context: context,
      isbn13: widget.isbn13,
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
        child: FutureBuilder<BookSearchCommonResponse>(
          future: Future.wait(_futureBookSearchDetailData()).then(
            (data) => _buildBookSearchDetailData(data),
          ),
          builder: (context, snapshot) {
            if (snapshot.hasError) {
              return const SingleDataError(
                errorMessage: '도서 정보를 불러오는데 실패했습니다.',
              );
            }

            if (snapshot.hasData) {
              BookSearchCommonResponse data = snapshot.data!;
              final BookSearchDetailResponseData bookSearchDetail =
                  data.bookSearchDetailResponseData;
              final BookInterestStatusResponseData bookInterestStatus =
                  data.bookInterestStatusResponseData;
              final BookRegisteredStatusResponseData bookRegisteredStatus =
                  data.bookRegisteredStatusResponseData;
              final BookCompletedStatusResponseData bookCompletedStatus =
                  data.bookCompletedStatusResponseData;

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
                          interested: bookInterestStatus.interested!,
                          registered: bookRegisteredStatus.registered!,
                          newRegistered: _registeredMyBook,
                          completed: bookCompletedStatus.completed!,
                          thumbnail: bookSearchDetail.thumbnail!,
                          title: bookSearchDetail.title!,
                          authors: bookSearchDetail.authors!,
                          interestCount: bookSearchDetail.interestCount!,
                          readCount: bookSearchDetail.readCount!,
                          holderCount: bookSearchDetail.holderCount!,
                          isbn13: bookSearchDetail.isbn13!,
                        ),
                        const BookDetailDivider(),
                        BookDetailInfo(
                          isbn13: bookSearchDetail.isbn13!,
                          publicationDate: bookSearchDetail.publicationDate!,
                          category: bookSearchDetail.category!,
                          pages: bookSearchDetail.pages!,
                          publisher: bookSearchDetail.publisher!,
                          starRating: bookSearchDetail.starRating!,
                          reviewCount: bookSearchDetail.reviewCount!,
                          link: bookSearchDetail.link!,
                          aladinStarRating: bookSearchDetail.aladinStarRating!,
                          onTapReview: _navigateToBookReviewScreen,
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
                  if (bookRegisteredStatus.registered! || _registeredMyBook)
                    BottomButton(
                      isScrollingDown: _isScrollingDown,
                      buttonText: '마이북으로 이동하기',
                      onTap: () {
                        Navigator.pushAndRemoveUntil(context, MaterialPageRoute(
                          builder: (context) {
                            return const RootTab(
                              tapIndex: 2,
                            );
                          },
                        ), (route) => false);
                      },
                    )
                  else
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

  List<Future<Object>> _futureBookSearchDetailData() {
    return [
      _bookSearchDetailResponse,
      _bookInterestStatusResponseData,
      _bookRegisteredStatusResponseData,
      _bookCompletedStatusResponseData,
    ];
  }

  BookSearchCommonResponse _buildBookSearchDetailData(List<Object> data) {
    final [
      bookSearchDetailResponseData,
      bookInterestStatusResponseData,
      bookRegisteredStatusResponseData,
      bookCompletedStatusResponseData,
    ] = data;
    return BookSearchCommonResponse(
      bookSearchDetailResponseData:
          bookSearchDetailResponseData as BookSearchDetailResponseData,
      bookInterestStatusResponseData:
          bookInterestStatusResponseData as BookInterestStatusResponseData,
      bookRegisteredStatusResponseData:
          bookRegisteredStatusResponseData as BookRegisteredStatusResponseData,
      bookCompletedStatusResponseData:
          bookCompletedStatusResponseData as BookCompletedStatusResponseData,
    );
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

  void _onTapSaveMyBook(String isbn13) async {
    await _bookRepository.createMyBook(
      context: context,
      userId: _userId,
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

    setState(() {
      _registeredMyBook = true;
    });

    Future.delayed(const Duration(seconds: 1), () {
      Navigator.of(context).pop();
    });
  }

  void _navigateToBookReviewScreen() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => SearchDetailReviewScreen(
          isbn13: widget.isbn13,
        ),
      ),
    ).then(
      (value) => setState(() {
        _bookSearchDetailResponse =
            _searchRepository.getBookSearchDetailAndSaveBookResponse(
          context: context,
          userId: _userId,
          isbn13: widget.isbn13,
        );
      }),
    );
  }
}
