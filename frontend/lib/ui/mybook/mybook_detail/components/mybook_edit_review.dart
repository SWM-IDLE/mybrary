import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:html/parser.dart';
import 'package:mybrary/data/repository/book_repository.dart';
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

  double? newStarRating;

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
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
                Center(
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
                ),
                const SizedBox(height: 20.0),
                Padding(
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
                      Row(
                        children: [
                          starRatingRow(
                            newStarRating ?? widget.starRating,
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
                                children:
                                    List<Widget>.generate(51, (int index) {
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
                      ),
                      const SizedBox(height: 16.0),
                      TextFormField(
                        maxLines: 8,
                        maxLength: 500,
                        controller: widget.contentController,
                        scrollPadding: EdgeInsets.only(
                          bottom: MediaQuery.of(context).viewInsets.bottom,
                        ),
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
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 20.0),
              ],
            ),
          ),
        ],
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
                    userId: 'testId',
                    myBookId: widget.myBookId!,
                    content: widget.contentController.text,
                    starRating: newStarRating ?? widget.starRating,
                  );

                  _showSavedMyReview();
                }
              : () async {
                  await _bookRepository.updateMyBookReview(
                    userId: 'testId',
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
}
