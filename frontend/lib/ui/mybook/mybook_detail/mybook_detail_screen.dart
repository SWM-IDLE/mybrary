import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/book/mybook_common_data.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/bottom_button.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_detail_header.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_record.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_review.dart';

class MyBookDetailScreen extends StatefulWidget {
  final int myBookId;

  const MyBookDetailScreen({
    required this.myBookId,
    super.key,
  });

  @override
  State<MyBookDetailScreen> createState() => _MyBookDetailScreenState();
}

class _MyBookDetailScreenState extends State<MyBookDetailScreen> {
  final _bookRepository = BookRepository();

  final ScrollController _myBookDetailScrollController = ScrollController();
  late Future<MyBookDetailResponseData> _myBookDetail;
  late Future<MyBookReviewResponseData?> _myBookReview;

  late String _myBookAppBarTitle = '';
  late bool _isScrollingDown = true;
  late bool _isOverflowMyBookDetailHeader = false;
  final GlobalKey _myBookDetailHeaderKey = GlobalKey();

  @override
  void initState() {
    super.initState();

    _myBookDetail = _bookRepository.getMyBookDetail(
      userId: 'testId',
      myBookId: widget.myBookId,
    );
    _myBookReview = _bookRepository.getMyBookReview(
      myBookId: widget.myBookId,
    );

    _myBookDetailScrollController.addListener(_bookDetailScrollListener);
  }

  void _bookDetailScrollListener() {
    double? position;
    ScrollPosition scrollPosition = _myBookDetailScrollController.position;

    if (_myBookDetailHeaderKey.currentContext != null) {
      position = _myBookDetailHeaderKey.currentContext!.size!.height;
    }

    if (scrollPosition.pixels > position!) {
      setState(() {
        _isOverflowMyBookDetailHeader = true;
      });
    } else {
      setState(() {
        _isOverflowMyBookDetailHeader = false;
      });
    }

    if (scrollPosition.pixels ==
            _myBookDetailScrollController.position.maxScrollExtent ||
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
    _myBookDetailScrollController.removeListener(_bookDetailScrollListener);
    _myBookDetailScrollController.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: FutureBuilder<MyBookDetailCommonData>(
        future: Future.wait(_futureMyBookDetailData())
            .then((data) => _buildMyBookDetailData(data)),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                _myBookDetailAppBar(appBarTitle: ''),
                const SliverToBoxAdapter(
                  child: DataError(
                    errorMessage: '마이북 내용을 불러오는데 실패했습니다.',
                  ),
                )
              ],
            );
          }

          if (snapshot.hasData) {
            MyBookDetailCommonData? data = snapshot.data;
            final MyBookDetailResponseData myBookDetailData =
                data!.myBooksResponseData;
            final MyBookReviewResponseData? myBookReviewData =
                data.myBookReviewResponseData;

            _myBookAppBarTitle = myBookDetailData.book!.title!;

            return Stack(
              children: [
                CustomScrollView(
                  controller: _myBookDetailScrollController,
                  physics: const BouncingScrollPhysics(
                    parent: AlwaysScrollableScrollPhysics(),
                  ),
                  slivers: [
                    _myBookDetailAppBar(
                      appBarTitle: _isOverflowMyBookDetailHeader
                          ? _myBookAppBarTitle
                          : '',
                      myBookId: widget.myBookId,
                      myBookTitle: myBookDetailData.book!.title!,
                    ),
                    MyBookDetailHeader(
                      headerKey: _myBookDetailHeaderKey,
                      thumbnail: myBookDetailData.book!.thumbnailUrl!,
                      title: myBookDetailData.book!.title!,
                      authors: myBookDetailData.book!.authors!,
                    ),
                    _myBookDetailDivider(),
                    MyBookRecord(
                      meaningTag: myBookDetailData.meaningTag!,
                      readStatus: myBookDetailData.readStatus!,
                      showable: myBookDetailData.showable!,
                      shareable: myBookDetailData.shareable!,
                      exchangeable: myBookDetailData.exchangeable!,
                      startDateOfPossession:
                          myBookDetailData.startDateOfPossession!,
                    ),
                    _myBookDetailDivider(),
                    if (myBookReviewData == null) _hasNoReview(),
                    if (myBookReviewData != null)
                      MyBookReview(
                        content: myBookReviewData.content!,
                        starRating: myBookReviewData.starRating!,
                        createdAt: myBookReviewData.createdAt!,
                      ),
                    const SliverToBoxAdapter(
                      child: SizedBox(height: 70.0),
                    ),
                  ],
                ),
                BottomButton(
                  isScrollingDown: _isScrollingDown,
                  buttonText: '마이북 기록 열기',
                )
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  SliverPadding _hasNoReview() {
    return SliverPadding(
      padding: const EdgeInsets.symmetric(horizontal: 20.0),
      sliver: SliverToBoxAdapter(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 10.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  '마이 리뷰',
                  style: commonSubBoldStyle,
                ),
                Text(
                  '작성하기',
                  style: bookDetailDescriptionStyle.copyWith(
                    decoration: TextDecoration.underline,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20.0),
            Row(
              children: [
                Row(
                  children: List.generate(5, (index) => index)
                      .map(
                        (e) => Image.asset(
                          'assets/img/icon/star.png',
                          // 별점 표시. 예로, 3.3은 3점이며 4.8은 4점으로 표시
                          color: bookStarDisabledColor,
                        ),
                      )
                      .toList(),
                ),
                const SizedBox(width: 10.0),
                Text(
                  '0.0',
                  style: commonSubBoldStyle.copyWith(
                    fontSize: 24.0,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 10.0),
            const Text(
              '마이 리뷰를 등록해보세요.',
              style: bookDetailDescriptionStyle,
            ),
            const SizedBox(height: 20.0),
          ],
        ),
      ),
    );
  }

  List<Future<Object?>> _futureMyBookDetailData() {
    return [
      _myBookDetail,
      _myBookReview,
    ];
  }

  MyBookDetailCommonData _buildMyBookDetailData(List<Object?> data) {
    final [myBookDetail, myBookReview] = data;
    return MyBookDetailCommonData(
      myBooksResponseData: myBookDetail as MyBookDetailResponseData,
      myBookReviewResponseData: myBookReview as MyBookReviewResponseData?,
    );
  }

  SliverAppBar _myBookDetailAppBar({
    required String appBarTitle,
    int? myBookId,
    String? myBookTitle,
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
      actions: myBookId == null && myBookTitle == null
          ? []
          : [
              IconButton(
                visualDensity: VisualDensity.compact,
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
              IconButton(
                visualDensity: VisualDensity.compact,
                onPressed: () async {
                  await showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return AlertDialog(
                        content: Wrap(
                          alignment: WrapAlignment.center,
                          children: [
                            const Text(
                              '선택한 도서를 마이북에서',
                              style: confirmButtonTextStyle,
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Text(
                                  '삭제',
                                  style: confirmButtonTextStyle.copyWith(
                                    color: commonRedColor,
                                  ),
                                ),
                                const Text(
                                  '하시겠습니까?',
                                  style: confirmButtonTextStyle,
                                ),
                              ],
                            ),
                          ],
                        ),
                        contentPadding: const EdgeInsets.only(
                          top: 24.0,
                          bottom: 16.0,
                        ),
                        actionsAlignment: MainAxisAlignment.center,
                        buttonPadding:
                            const EdgeInsets.symmetric(horizontal: 8.0),
                        actions: [
                          Row(
                            children: [
                              _confirmButton(
                                onTap: () {
                                  Navigator.of(context).pop();
                                },
                                buttonText: '취소',
                                isCancel: true,
                              ),
                              _confirmButton(
                                onTap: () {
                                  _bookRepository.deleteMyBook(
                                    userId: 'testId',
                                    myBookId: myBookId!,
                                  );

                                  Future.delayed(
                                      const Duration(
                                        milliseconds: 500,
                                      ), () {
                                    Navigator.of(context).pop();
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                        content: Text(
                                          '선택한 도서가 삭제되었습니다.',
                                          style: commonSnackBarMessageStyle,
                                        ),
                                        duration: Duration(seconds: 1),
                                      ),
                                    );
                                    Navigator.of(context).pop();
                                  });
                                },
                                buttonText: '삭제',
                                isCancel: false,
                              ),
                            ],
                          ),
                        ],
                      );
                    },
                  );
                },
                icon: const Icon(
                  Icons.bookmark_remove_outlined,
                ),
              ),
            ],
    );
  }

  SliverPadding _myBookDetailDivider() {
    return const SliverPadding(
      padding: EdgeInsets.symmetric(vertical: 12.0),
      sliver: SliverToBoxAdapter(
        child: Divider(
          height: 1,
          thickness: 6,
          color: greyF1F2F5,
        ),
      ),
    );
  }

  Widget _confirmButton({
    required GestureTapCallback? onTap,
    required String buttonText,
    required bool isCancel,
  }) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 4.0),
        child: InkWell(
          onTap: onTap,
          child: Container(
            height: 46.0,
            decoration: BoxDecoration(
              color: isCancel ? greyF1F2F5 : primaryColor,
              borderRadius: BorderRadius.circular(4.0),
            ),
            child: Center(
              child: Text(
                buttonText,
                style: commonSubBoldStyle.copyWith(
                  color: isCancel ? commonBlackColor : commonWhiteColor,
                  fontSize: 14.0,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
