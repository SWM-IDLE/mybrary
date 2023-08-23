import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/search/search_detail_review/components/review_item.dart';
import 'package:mybrary/utils/logics/book_utils.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class SearchDetailReviewScreen extends StatefulWidget {
  final String isbn13;

  const SearchDetailReviewScreen({
    required this.isbn13,
    super.key,
  });

  @override
  State<SearchDetailReviewScreen> createState() =>
      _SearchDetailReviewScreenState();
}

class _SearchDetailReviewScreenState extends State<SearchDetailReviewScreen> {
  final _searchRepository = SearchRepository();
  final _bookRepository = BookRepository();

  final ScrollController _reviewScrollController = ScrollController();

  late Future<BookDetailReviewResponseData> _searchBookDetailReviewData;

  late List<MyBookReviewList>? _myBookReviewList = [];
  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _searchBookDetailReviewData = _searchRepository.getBookSearchDetailReviews(
      context: context,
      isbn13: widget.isbn13,
    );
  }

  @override
  void dispose() {
    super.dispose();

    _reviewScrollController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: CustomScrollView(
        physics: const BouncingScrollPhysics(
          parent: AlwaysScrollableScrollPhysics(),
        ),
        slivers: [
          commonSliverAppBar(
            appBarTitle: '마이 리뷰',
          ),
          SliverList(
            delegate: SliverChildListDelegate([
              FutureBuilder<BookDetailReviewResponseData>(
                future: _searchBookDetailReviewData,
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return const DataError(
                      errorMessage: '마이 리뷰를 불러오는데 실패했습니다.',
                    );
                  }
                  if (snapshot.hasData) {
                    final reviewData = snapshot.data!;

                    if (_myBookReviewList!.isEmpty) {
                      _myBookReviewList = reviewData.myBookReviewList;
                    }

                    if (_myBookReviewList!.isNotEmpty) {
                      return Column(
                        children: [
                          _divider(),
                          _reviewHeader(reviewData),
                          _divider(),
                          ListView.separated(
                            controller: _reviewScrollController,
                            shrinkWrap: true,
                            padding: const EdgeInsets.only(top: 16.0),
                            itemCount: _myBookReviewList!.length,
                            itemBuilder: (context, index) {
                              final review = _myBookReviewList![index];

                              return ReviewItem(
                                review: review,
                                onTapDelete: () => _deleteReview(
                                  context: context,
                                  reviewId: _myBookReviewList![index].id!,
                                ),
                              );
                            },
                            separatorBuilder: (context, index) {
                              return Padding(
                                padding: const EdgeInsets.symmetric(
                                  vertical: 12.0,
                                ),
                                child: _divider(),
                              );
                            },
                          ),
                          const SizedBox(height: 30.0),
                        ],
                      );
                    } else {
                      return const DataError(
                        icon: Icons.rate_review_rounded,
                        errorMessage: '아직 작성된 마이 리뷰가 없어요!',
                      );
                    }
                  }

                  return const CircularLoading();
                },
              ),
            ]),
          ),
        ],
      ),
    );
  }

  Widget _reviewHeader(BookDetailReviewResponseData reviewData) {
    double originStarRatingAverage = reviewData.starRatingAverage!;
    double starRatingAverage =
        double.parse(originStarRatingAverage.toStringAsFixed(1));

    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        children: [
          starRatingRow(starRating: reviewData.starRatingAverage!),
          const SizedBox(width: 8.0),
          Text(
            '$starRatingAverage (${reviewData.myBookReviewList!.length})',
            style: starRatingTextStyle,
          ),
        ],
      ),
    );
  }

  Widget _divider() {
    return const Divider(
      height: 1,
      thickness: 1,
      color: greyF1F2F5,
    );
  }

  void _deleteReview({
    required BuildContext context,
    required int reviewId,
  }) async {
    await showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text(
            '삭제',
            style: commonSubBoldStyle,
            textAlign: TextAlign.center,
          ),
          content: const Text(
            '정말 리뷰를 삭제하시겠습니까?',
            style: confirmButtonTextStyle,
            textAlign: TextAlign.center,
          ),
          contentPadding: const EdgeInsets.only(
            top: 24.0,
            bottom: 16.0,
          ),
          actionsAlignment: MainAxisAlignment.center,
          buttonPadding: const EdgeInsets.symmetric(horizontal: 8.0),
          actions: [
            Row(
              children: [
                confirmButton(
                  onTap: () {
                    Navigator.of(context).pop();
                  },
                  buttonText: '취소',
                  isCancel: true,
                ),
                confirmButton(
                  onTap: () async {
                    await _bookRepository.deleteMyBookReview(
                      context: context,
                      userId: _userId,
                      reviewId: reviewId,
                    );

                    if (!mounted) return;
                    showInterestBookMessage(
                      context: context,
                      snackBarText: '마이 리뷰가 삭제되었습니다.',
                    );
                    Navigator.of(context).pop();
                    setState(() {
                      _myBookReviewList!.removeWhere(
                        (review) => review.id == reviewId,
                      );
                    });
                  },
                  buttonText: '삭제하기',
                  isCancel: false,
                ),
              ],
            ),
          ],
        );
      },
    );
  }
}
