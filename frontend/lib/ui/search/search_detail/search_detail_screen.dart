import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/search/book_search_detail_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/book_detail_divider.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
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
  final GlobalKey _bookDetailHeaderKey = GlobalKey();

  late String _bookTitle = '';
  late Future<BookSearchDetailResponseData> _bookSearchDetailResponse;

  bool _isOverflowBookDetailHeader = false;
  late bool _isOnTapHeart = false;
  late int _newInterestCount = 0;

  final ScrollController _bookDetailScrollController = ScrollController();

  late dynamic position;

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
          _isOnTapHeart = response.interested!;
          _newInterestCount = response.interestCount!;
        });
      },
    );

    _bookDetailScrollController.addListener(_scrollListener);
  }

  void _scrollListener() {
    double? position;
    if (_bookDetailHeaderKey.currentContext != null) {
      position = _bookDetailHeaderKey.currentContext!.size!.height;
    }
    if (_bookDetailScrollController.position.pixels > position!) {
      setState(() {
        _isOverflowBookDetailHeader = true;
      });
    } else {
      setState(() {
        _isOverflowBookDetailHeader = false;
      });
    }
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
              return const Center(
                child: Text('도서 정보를 불러오는데 실패했습니다.'),
              );
            }

            if (snapshot.hasData) {
              final bookSearchDetail = snapshot.data!;
              bool interested = bookSearchDetail.interested!;
              int interestCount = bookSearchDetail.interestCount!;

              _bookTitle = bookSearchDetail.title!;

              return SingleChildScrollView(
                controller: _bookDetailScrollController,
                physics: const BouncingScrollPhysics(),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    BookDetailHeader(
                      key: _bookDetailHeaderKey,
                      onTapInterestBook: () async {
                        final result =
                            await _bookRepository.createOrDeleteInterestBook(
                          userId: 'testId',
                          isbn13: bookSearchDetail.isbn13!,
                        );

                        setState(() {
                          _isOnTapHeart = result.interested!;

                          _isInterestBook(
                            interested,
                            interestCount,
                            context,
                          );
                        });
                      },
                      interested: interested,
                      isOnTapHeart: _isOnTapHeart,
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
                    const SizedBox(height: 50.0),
                  ],
                ),
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
    if (!interested && _isOnTapHeart) {
      _newInterestCount = interestCount + 1;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
      );
    } else if (interested && _isOnTapHeart == false) {
      _newInterestCount = interestCount - 1;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서가 삭제되었습니다.',
      );
    } else if (interested && _isOnTapHeart) {
      _newInterestCount = interestCount;
      _showInterestBookMessage(
        context: context,
        snackBarText: '관심 도서에 담겼습니다.',
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
  }) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(snackBarText),
        duration: const Duration(
          seconds: 1,
        ),
      ),
    );
  }
}
