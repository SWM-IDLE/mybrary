import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/book/mybook_common_data.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/model/book/mybook_record_reponse.dart';
import 'package:mybrary/data/model/book/mybook_review_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/bottom_button.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_detail_header.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_detail_record.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_detail_review.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_edit_review.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class MyBookDetailScreen extends StatefulWidget {
  final int myBookId;
  final String? userId;

  const MyBookDetailScreen({
    required this.myBookId,
    this.userId,
    super.key,
  });

  @override
  State<MyBookDetailScreen> createState() => _MyBookDetailScreenState();
}

class _MyBookDetailScreenState extends State<MyBookDetailScreen> {
  final _bookRepository = BookRepository();

  late Future<MyBookDetailResponseData> _myBookDetail;
  late Future<MyBookReviewResponseData?> _myBookReview;

  final GlobalKey _myBookDetailHeaderKey = GlobalKey();

  final ScrollController _myBookDetailScrollController = ScrollController();
  final TextEditingController _meaningTagQuoteController =
      TextEditingController();

  late TextEditingController _myBookReviewContentController =
      TextEditingController();

  late String _myBookAppBarTitle = '';
  late bool _isScrollingDown = true;
  late bool _isOverflowMyBookDetailHeader = false;

  late bool _originalShowable;
  late bool _originalShareable;
  late bool _originalExchangeable;
  late String _originalReadStatus;

  bool? _newShowable;
  bool? _newShareable;
  bool? _newExchangeable;
  String? _newReadStatus;
  String? _newMeaningTagColorCode;
  String? _newMeaningTagQuote;
  String? _newStartDateOfPossession;

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _bookRepository
        .getMyBookDetail(
          context: context,
          userId: widget.userId ?? _userId,
          myBookId: widget.myBookId,
        )
        .then(
          (data) => {
            _meaningTagQuoteController.text = data.meaningTag!.quote!,
            _originalReadStatus = data.readStatus!,
            _originalShowable = data.showable!,
            _originalShareable = data.shareable!,
            _originalExchangeable = data.exchangeable!,
          },
        );

    _bookRepository
        .getMyBookReview(
      context: context,
      myBookId: widget.myBookId,
    )
        .then((data) {
      _myBookReviewContentController = TextEditingController(
        text: data == null ? '' : data.content,
      );
    });

    _myBookDetail = _bookRepository.getMyBookDetail(
      context: context,
      userId: widget.userId ?? _userId,
      myBookId: widget.myBookId,
    );
    _myBookReview = _bookRepository.getMyBookReview(
      context: context,
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
    _meaningTagQuoteController.dispose();

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
            MyBookDetailResponseData myBookDetailData =
                data!.myBooksResponseData;
            MyBookReviewResponseData? myBookReviewData =
                data.myBookReviewResponseData;

            Book myBookInfo = myBookDetailData.book!;
            _myBookAppBarTitle = myBookInfo.title!;

            List<String>? newDate;
            DateTime? newDateTime;
            if (_newStartDateOfPossession != null) {
              newDate = _newStartDateOfPossession!.split('.');
              final [year, month, day] = newDate;

              newDateTime =
                  DateTime(int.parse(year), int.parse(month), int.parse(day));
            }

            DateTime dateTime = _newStartDateOfPossession == null
                ? DateTime.parse(myBookDetailData.startDateOfPossession!
                    .replaceAll('.', '-'))
                : DateTime.parse(
                    newDateTime.toString(),
                  );

            String colorCode = _newMeaningTagColorCode ??
                myBookDetailData.meaningTag!.colorCode!.replaceAll('#', '');

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
                      myBookTitle: myBookInfo.title!,
                    ),
                    MyBookDetailHeader(
                      headerKey: _myBookDetailHeaderKey,
                      thumbnail: myBookInfo.thumbnailUrl!,
                      title: myBookInfo.title!,
                      authors: myBookInfo.authors!,
                    ),
                    _myBookDetailDivider(),
                    MyBookDetailRecord(
                      readStatus:
                          _newReadStatus ?? myBookDetailData.readStatus!,
                      showable: _newShowable ?? myBookDetailData.showable!,
                      shareable: _newShareable ?? myBookDetailData.shareable!,
                      exchangeable:
                          _newExchangeable ?? myBookDetailData.exchangeable!,
                      startDateOfPossession: _newStartDateOfPossession ??
                          myBookDetailData.startDateOfPossession!,
                      meaningTagColorCode: _newMeaningTagColorCode ?? colorCode,
                      meaningTagQuote: _newMeaningTagQuote ??
                          myBookDetailData.meaningTag!.quote!,
                    ),
                    _myBookDetailDivider(),
                    if (myBookReviewData == null)
                      _hasNoReview(
                        thumbnailUrl: myBookInfo.thumbnailUrl!,
                        title: myBookInfo.title!,
                        authors: myBookInfo.authors!,
                        starRating: myBookInfo.starRating!,
                        contentController: _myBookReviewContentController,
                      ),
                    if (myBookReviewData != null)
                      MyBookDetailReview(
                        content: myBookReviewData.content!,
                        starRating: myBookReviewData.starRating!,
                        createdAt: myBookReviewData.createdAt!,
                        thumbnailUrl: myBookInfo.thumbnailUrl!,
                        title: myBookInfo.title!,
                        authors: myBookInfo.authors!,
                        contentController: _myBookReviewContentController,
                        nextToMyBookReview: _nextToMyBookReview,
                        reviewId: myBookReviewData.id!,
                        userId: widget.userId,
                      ),
                    SliverToBoxAdapter(
                      child:
                          SizedBox(height: widget.userId == null ? 30.0 : 70.0),
                    ),
                  ],
                ),
                if (widget.userId == null)
                  BottomButton(
                    isScrollingDown: _isScrollingDown,
                    buttonText: '마이북 기록 열기',
                    onTap: () {
                      _showMyBookRecordEdit(context, colorCode, dateTime);
                    },
                  ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  Future<dynamic> _showMyBookRecordEdit(
      BuildContext context, String colorCode, DateTime dateTime) {
    return showMenuBottomSheet(
      context,
      StatefulBuilder(
        builder: (BuildContext context, StateSetter bottomState) {
          final bottomInset = MediaQuery.of(context).viewInsets.bottom;

          return Padding(
            padding: EdgeInsets.only(bottom: bottomInset),
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(
                      left: 16.0,
                      right: 16.0,
                      top: 24.0,
                    ),
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                        vertical: 8.0,
                      ),
                      decoration: BoxDecoration(
                        color: greyF1F2F5,
                        borderRadius: BorderRadius.circular(50.0),
                      ),
                      alignment: Alignment.center,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          _readStatusButtonItem(
                            buttonText: '읽기전',
                            currentReadStatus: _originalReadStatus == 'TO_READ',
                            onTapReadStatus: () {
                              bottomState(
                                () => _originalReadStatus = 'TO_READ',
                              );
                            },
                          ),
                          _readStatusButtonItem(
                            buttonText: '읽는중',
                            currentReadStatus: _originalReadStatus == 'READING',
                            onTapReadStatus: () {
                              bottomState(
                                () => _originalReadStatus = 'READING',
                              );
                            },
                          ),
                          _readStatusButtonItem(
                            buttonText: '완독함',
                            currentReadStatus:
                                _originalReadStatus == 'COMPLETED',
                            onTapReadStatus: () {
                              bottomState(
                                () => _originalReadStatus = 'COMPLETED',
                              );
                            },
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(
                    height: 8.0,
                  ),
                  Padding(
                    padding: const EdgeInsets.all(24.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          '나에게 이 책은',
                          style: myBookSortTextStyle,
                        ),
                        const SizedBox(height: 16.0),
                        Wrap(
                          spacing: 8.0,
                          runSpacing: 10.0,
                          children: meaningTagColors
                              .map(
                                (color) => InkWell(
                                  onTap: () {
                                    bottomState(() {
                                      colorCode = color;
                                    });
                                  },
                                  child: renderColor(
                                    color,
                                    colorCode == color,
                                  ),
                                ),
                              )
                              .toList(),
                        ),
                        const SizedBox(height: 10.0),
                        _myBookRecordInput(
                          context: context,
                          controller: _meaningTagQuoteController,
                          hintText: '내 삶을 돌아보게 만든 책',
                        ),
                        const SizedBox(height: 16.0),
                        const Text(
                          '소장일',
                          style: myBookSortTextStyle,
                        ),
                        const SizedBox(height: 8.0),
                        CupertinoButton(
                          padding: const EdgeInsets.all(0.0),
                          onPressed: () => showCupertinoPicker(
                            context,
                            CupertinoDatePicker(
                              initialDateTime: dateTime,
                              mode: CupertinoDatePickerMode.date,
                              use24hFormat: true,
                              maximumDate: DateTime.now(),
                              onDateTimeChanged: (DateTime newDateTime) {
                                bottomState(
                                  () => dateTime = newDateTime,
                                );
                              },
                            ),
                          ),
                          child: Container(
                            width: double.infinity,
                            padding: const EdgeInsets.symmetric(
                              vertical: 12.0,
                            ),
                            decoration: const BoxDecoration(
                              border: Border(
                                bottom: BorderSide(
                                  color: inputBorderColor,
                                  width: 1.0,
                                ),
                              ),
                            ),
                            child: Text(
                              '${dateTime.year}.${dateTime.month}.${dateTime.day}',
                              style: commonSubMediumStyle,
                            ),
                          ),
                        ),
                        const SizedBox(height: 24.0),
                        Row(
                          children: [
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const Text(
                                    '교환/나눔',
                                    style: myBookSortTextStyle,
                                  ),
                                  const SizedBox(height: 18.0),
                                  _checkRecordItemRow(bottomState),
                                  const SizedBox(height: 10.0),
                                ],
                              ),
                            ),
                            Expanded(
                              child: Column(
                                children: [
                                  const Text(
                                    '비공개',
                                    style: myBookSortTextStyle,
                                  ),
                                  const SizedBox(height: 10.0),
                                  CupertinoSwitch(
                                    value: !_originalShowable,
                                    onChanged: (value) {
                                      bottomState(() {
                                        _originalShowable = !value;
                                      });
                                    },
                                    activeColor: primaryColor,
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                  _myBookRecordButton(
                    context: context,
                    dateTime: dateTime,
                    colorCode: colorCode,
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Row _checkRecordItemRow(StateSetter bottomState) {
    return Row(
      children: [
        Row(
          children: [
            InkWell(
              onTap: () {
                bottomState(
                  () => _originalExchangeable = !_originalExchangeable,
                );
              },
              child: SvgPicture.asset(
                'assets/svg/icon/small/checkbox_${_originalExchangeable ? 'green' : 'grey'}.svg',
              ),
            ),
            const SizedBox(width: 8.0),
            const Text(
              '교환',
              style: myBookSortTextStyle,
            ),
            const SizedBox(width: 24.0),
            InkWell(
              onTap: () {
                bottomState(
                  () => _originalShareable = !_originalShareable,
                );
              },
              child: SvgPicture.asset(
                'assets/svg/icon/small/checkbox_${_originalShareable ? 'green' : 'grey'}.svg',
              ),
            ),
            const SizedBox(width: 8.0),
            const Text(
              '나눔',
              style: myBookSortTextStyle,
            ),
          ],
        ),
      ],
    );
  }

  Widget _myBookRecordInput({
    required BuildContext context,
    required TextEditingController controller,
    required String hintText,
    TextInputType? keyboardType,
  }) {
    return TextFormField(
      controller: controller,
      maxLength: 20,
      style: commonSubMediumStyle,
      scrollPadding: EdgeInsets.only(
        bottom: MediaQuery.of(context).viewInsets.bottom,
      ),
      onEditingComplete: () {
        FocusScope.of(context).unfocus();
      },
      keyboardType: keyboardType,
      decoration: InputDecoration(
        contentPadding: const EdgeInsets.symmetric(
          horizontal: 4.0,
          vertical: 16.0,
        ),
        hintText: hintText,
        hintStyle: inputHintStyle,
        counter: const SizedBox.shrink(),
        border: introInputBorderBottomStyle,
        enabledBorder: introInputBorderBottomStyle,
        focusedBorder: introInputBorderBottomStyle,
        errorStyle: const TextStyle(
          decoration: TextDecoration.none,
        ),
      ),
    );
  }

  Widget renderColor(String color, bool isSelected) {
    return Container(
      decoration: BoxDecoration(
        color: Color(
          int.parse(
            'FF$color',
            radix: 16,
          ),
        ),
        border: isSelected ? Border.all(color: Colors.black, width: 3.0) : null,
        shape: BoxShape.circle,
      ),
      width: 36.0,
      height: 36.0,
    );
  }

  Widget _readStatusButtonItem({
    required String buttonText,
    required bool currentReadStatus,
    required GestureTapCallback? onTapReadStatus,
  }) {
    return InkWell(
      onTap: onTapReadStatus,
      child: Container(
        padding: const EdgeInsets.symmetric(
          horizontal: 32.0,
          vertical: 8.0,
        ),
        decoration: BoxDecoration(
          color: currentReadStatus ? commonWhiteColor : greyF1F2F5,
          borderRadius: BorderRadius.circular(60.0),
        ),
        child: Text(
          buttonText,
          style: commonButtonTextStyle.copyWith(
            color: currentReadStatus ? primaryColor : grey262626,
          ),
        ),
      ),
    );
  }

  Padding _myBookRecordButton({
    required BuildContext context,
    required DateTime dateTime,
    required String colorCode,
  }) {
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
                _bookRepository
                    .getMyBookDetail(
                      context: context,
                      userId: _userId,
                      myBookId: widget.myBookId,
                    )
                    .then(
                      (data) => {
                        setState(() {
                          _meaningTagQuoteController.text =
                              data.meaningTag!.quote!;
                          _originalReadStatus = data.readStatus!;
                          _originalShowable = data.showable!;
                          _originalShareable = data.shareable!;
                          _originalExchangeable = data.exchangeable!;
                        }),
                      },
                    );
              },
              child: Container(
                color: greyDDDDDD,
                padding: const EdgeInsets.symmetric(
                  vertical: 12.0,
                ),
                child: const Text(
                  '취소',
                  style: commonSubMediumStyle,
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ),
          Expanded(
            flex: 2,
            child: InkWell(
              onTap: () async {
                await _bookRepository.updateMyBookRecord(
                  context: context,
                  userId: _userId,
                  myBookId: widget.myBookId,
                  myBookRecordData: MyBookRecordResponseData(
                    meaningTag: MeaningTag(
                      quote: _meaningTagQuoteController.text,
                      colorCode: '#$colorCode',
                    ),
                    startDateOfPossession: dateTime.toIso8601String(),
                    readStatus: _originalReadStatus,
                    showable: _originalShowable,
                    shareable: _originalShareable,
                    exchangeable: _originalExchangeable,
                  ),
                );

                setState(() {
                  _newMeaningTagColorCode = colorCode;
                  _newReadStatus = _originalReadStatus;
                  _newShowable = _originalShowable;
                  _newShareable = _originalShareable;
                  _newExchangeable = _originalExchangeable;
                  _newMeaningTagQuote = _meaningTagQuoteController.text;
                  _newStartDateOfPossession =
                      '${dateTime.year}.${dateTime.month}.${dateTime.day}';
                });

                if (!mounted) return;
                Navigator.of(context).pop();
              },
              child: Container(
                color: primaryColor,
                padding: const EdgeInsets.symmetric(
                  vertical: 12.0,
                ),
                child: Text(
                  '저장',
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

  SliverPadding _hasNoReview({
    required String thumbnailUrl,
    required String title,
    required List<String> authors,
    required double starRating,
    required TextEditingController contentController,
  }) {
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
                if (widget.userId == null)
                  InkWell(
                    onTap: () async {
                      _nextToMyBookReview(
                        thumbnailUrl: thumbnailUrl,
                        title: title,
                        authors: authors,
                        starRating: starRating,
                        contentController: contentController,
                        isCreateReview: true,
                        myBookId: widget.myBookId,
                      );
                    },
                    child: Text(
                      '작성하기',
                      style: bookDetailDescriptionStyle.copyWith(
                        decoration: TextDecoration.underline,
                      ),
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
            Text(
              widget.userId != null ? '마이 리뷰를 등록해보세요.' : '작성된 리뷰가 없습니다.',
              style: bookDetailDescriptionStyle,
            ),
            const SizedBox(height: 20.0),
          ],
        ),
      ),
    );
  }

  void _nextToMyBookReview({
    required bool isCreateReview,
    required String thumbnailUrl,
    required String title,
    required List<String> authors,
    required double? starRating,
    required TextEditingController contentController,
    int? myBookId,
    int? reviewId,
  }) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => MyBookEditReview(
          isCreateReview: isCreateReview,
          thumbnailUrl: thumbnailUrl,
          title: title,
          authors: authors,
          starRating: starRating ?? 0.0,
          contentController: _myBookReviewContentController,
          myBookId: myBookId,
          reviewId: reviewId,
        ),
      ),
    ).then(
      (value) => setState(() {
        _myBookReview = _bookRepository.getMyBookReview(
          context: context,
          myBookId: widget.myBookId,
        );
        _bookRepository
            .getMyBookReview(
              context: context,
              myBookId: widget.myBookId,
            )
            .then((data) => _myBookReviewContentController.text =
                data == null ? '' : data.content!);
      }),
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
      actions: [
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
        if (widget.userId == null)
          Padding(
            padding: const EdgeInsets.only(right: 12.0),
            child: IconButton(
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
                                  context: context,
                                  userId: _userId,
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
              icon: SvgPicture.asset(
                'assets/svg/icon/small/book_remove.svg',
              ),
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
