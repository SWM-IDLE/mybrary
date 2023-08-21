import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:html/parser.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class MyBookEditReview extends StatefulWidget {
  final bool isCreateReview;
  final String thumbnailUrl;
  final String title;
  final List<String> authors;
  final double starRating;
  final TextEditingController contentController;

  final int? myBookId;
  final int? reviewId;

  const MyBookEditReview({
    required this.isCreateReview,
    required this.thumbnailUrl,
    required this.title,
    required this.authors,
    required this.starRating,
    required this.contentController,
    this.myBookId,
    this.reviewId,
    super.key,
  });

  @override
  State<MyBookEditReview> createState() => _MyBookEditReviewState();
}

class _MyBookEditReviewState extends State<MyBookEditReview> {
  final _bookRepository = BookRepository();
  final _userId = UserState.userId;

  double? newStarRating;
  String? newContent;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        return _onBackKey(context);
      },
      child: DefaultLayout(
        child: CustomScrollView(
          keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
          physics: const BouncingScrollPhysics(
            parent: AlwaysScrollableScrollPhysics(),
          ),
          slivers: [
            _myBookEditAppBar(context),
            SliverToBoxAdapter(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _reviewHeader(),
                  const SizedBox(height: 20.0),
                  _reviewBody(context),
                  const SizedBox(height: 20.0),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<bool> _onBackKey(BuildContext context) async {
    if (newStarRating != null || newContent != null) {
      return await showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: const Text(
              '저장 여부',
              style: commonSubBoldStyle,
              textAlign: TextAlign.center,
            ),
            content: const Text(
              '작성 중인 리뷰가 존재합니다. 저장하지 않고 뒤로 가시겠습니까?',
              style: confirmButtonTextStyle,
            ),
            contentPadding: const EdgeInsets.all(16.0),
            actionsAlignment: MainAxisAlignment.center,
            buttonPadding: const EdgeInsets.symmetric(horizontal: 8.0),
            actions: [
              Row(
                children: [
                  _confirmButton(
                    onTap: () {
                      Navigator.of(context).pop(false);
                    },
                    buttonText: '취소',
                    isCancel: true,
                  ),
                  _confirmButton(
                    onTap: () {
                      Navigator.of(context).pop(true);
                    },
                    buttonText: '저장없이 뒤로가기',
                    isCancel: false,
                  ),
                ],
              ),
            ],
          );
        },
      );
    } else {
      return true;
    }
  }

  Padding _reviewBody(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 18.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            parse(widget.title).documentElement!.text,
            style: commonEditTitleStyle.copyWith(
              fontSize: 16.0,
            ),
          ),
          const SizedBox(height: 2.0),
          Text(
            widget.authors.isNotEmpty
                ? '${widget.authors.map((author) => author).join(',')} 저'
                : '',
            style: commonEditContentStyle.copyWith(
              fontSize: 12.0,
            ),
          ),
          const SizedBox(height: 16.0),
          _editStarRating(context),
          const SizedBox(height: 16.0),
          _editContent(context),
        ],
      ),
    );
  }

  TextFormField _editContent(BuildContext context) {
    return TextFormField(
      maxLines: 8,
      maxLength: 500,
      controller: widget.contentController,
      scrollPadding: EdgeInsets.only(
        bottom: MediaQuery.of(context).viewInsets.bottom,
      ),
      onChanged: (String value) {
        setState(() {
          newContent = value;
        });
      },
      onEditingComplete: () {
        FocusScope.of(context).unfocus();
      },
      decoration: const InputDecoration(
        filled: true,
        fillColor: greyF1F2F5,
        contentPadding: EdgeInsets.all(16.0),
        hintText: '독서 리뷰를 남기고 기록해보세요.',
        hintStyle: inputHintStyle,
        border: introInputBorderStyle,
        enabledBorder: introInputBorderStyle,
        focusedBorder: introInputBorderStyle,
      ),
    );
  }

  Row _editStarRating(BuildContext context) {
    return Row(
      children: [
        starRatingRow(
          starRating: newStarRating ?? widget.starRating,
        ),
        const SizedBox(width: 8.0),
        CupertinoButton(
          padding: EdgeInsets.zero,
          onPressed: () => showCupertinoPicker(
            context,
            CupertinoPicker(
              itemExtent: 50,
              scrollController: FixedExtentScrollController(
                initialItem: newStarRating == null
                    ? (widget.starRating * 10).toInt()
                    : newStarRating!.toInt(),
              ),
              onSelectedItemChanged: (int index) {
                setState(() {
                  newStarRating = index / 10;
                });
              },
              children: List<Widget>.generate(51, (int index) {
                return Center(
                  child: Text(
                    (index / 10).toStringAsFixed(1),
                    style: const TextStyle(fontSize: 22),
                  ),
                );
              }),
            ),
          ),
          child: Text(
            newStarRating == null
                ? widget.starRating.toString()
                : newStarRating.toString(),
            style: commonSubBoldStyle.copyWith(
              height: 1.3,
              fontSize: 20.0,
              decoration: TextDecoration.underline,
            ),
          ),
        ),
      ],
    );
  }

  Center _reviewHeader() {
    return Center(
      child: Container(
        width: 176,
        height: 254,
        alignment: Alignment.center,
        decoration: ShapeDecoration(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10),
          ),
          shadows: [
            BoxShadow(
              color: commonBlackColor.withOpacity(0.3),
              blurRadius: 6,
              offset: const Offset(0, 4),
              spreadRadius: 0,
            )
          ],
          image: DecorationImage(
            image: NetworkImage(widget.thumbnailUrl),
            fit: BoxFit.fill,
          ),
        ),
      ),
    );
  }

  SliverAppBar _myBookEditAppBar(BuildContext context) {
    return SliverAppBar(
      elevation: 0,
      title: const Text('마이리뷰'),
      titleTextStyle: appBarTitleStyle.copyWith(
        fontSize: 16.0,
      ),
      pinned: true,
      centerTitle: true,
      backgroundColor: commonWhiteColor,
      foregroundColor: commonBlackColor,
      actions: [
        TextButton(
          onPressed: widget.isCreateReview
              ? () async {
                  await _bookRepository.createMyBookReview(
                    context: context,
                    userId: _userId,
                    myBookId: widget.myBookId!,
                    content: widget.contentController.text,
                    starRating: newStarRating ?? widget.starRating,
                  );

                  _showSavedMyReview();
                }
              : () async {
                  await _bookRepository.updateMyBookReview(
                    context: context,
                    userId: _userId,
                    reviewId: widget.reviewId!,
                    content: widget.contentController.text,
                    starRating: newStarRating ?? widget.starRating,
                  );

                  _showSavedMyReview();
                },
          style: disableAnimationButtonStyle,
          child: const Text(
            '저장',
            style: saveTextButtonStyle,
          ),
        ),
      ],
    );
  }

  void _showSavedMyReview() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('마이 리뷰가 저장되었습니다.'),
        duration: Duration(
          seconds: 1,
        ),
      ),
    );
    Navigator.pop(context);
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
