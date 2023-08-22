import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/data/repository/search_repository.dart';
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

  late Future<BookDetailReviewResponseData> _searchBookDetailReviewData;

  final ScrollController _reviewScrollController = ScrollController();

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

                    if (reviewData.reviewCount! > 0) {
                      return Column(
                        children: [
                          _divider(),
                          _reviewHeader(reviewData),
                          _divider(),
                          ListView.separated(
                            controller: _reviewScrollController,
                            shrinkWrap: true,
                            padding: const EdgeInsets.only(top: 16.0),
                            itemCount: reviewData.reviewCount!,
                            itemBuilder: (context, index) {
                              final review =
                                  reviewData.myBookReviewList![index];

                              return ReviewItem(
                                review: review,
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
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        children: [
          starRatingRow(starRating: reviewData.starRatingAverage!),
          const SizedBox(width: 8.0),
          Text(
            '${reviewData.starRatingAverage} (${reviewData.reviewCount})',
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
}
