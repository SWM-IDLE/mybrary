import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/data/repository/search_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
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

  late bool isExpanded = false;

  @override
  void initState() {
    super.initState();

    _searchBookDetailReviewData = _searchRepository.getBookSearchDetailReviews(
      context: context,
      isbn13: widget.isbn13,
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: CustomScrollView(
        physics: const NeverScrollableScrollPhysics(),
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

                    return reviewData.reviewCount! > 0
                        ? Column(
                            children: [
                              _divider(),
                              Padding(
                                padding: const EdgeInsets.all(16.0),
                                child: Row(
                                  children: [
                                    starRatingRow(
                                        starRating:
                                            reviewData.starRatingAverage!),
                                    const SizedBox(width: 8.0),
                                    Text(
                                      '${reviewData.starRatingAverage} (${reviewData.reviewCount})',
                                      style: starRatingTextStyle,
                                    ),
                                  ],
                                ),
                              ),
                              _divider(),
                              SizedBox(
                                height: MediaQuery.of(context).size.height,
                                child: ListView.separated(
                                  shrinkWrap: true,
                                  physics: const BouncingScrollPhysics(
                                    parent: AlwaysScrollableScrollPhysics(),
                                  ),
                                  itemCount: reviewData.reviewCount!,
                                  itemBuilder: (context, index) {
                                    final review =
                                        reviewData.myBookReviewList![index];
                                    List<String> publicationDate = review
                                        .createdAt!
                                        .substring(0, 10)
                                        .split('-');

                                    final [year, month, day] = publicationDate;

                                    return Padding(
                                      padding: const EdgeInsets.symmetric(
                                        horizontal: 16.0,
                                        vertical: 4.0,
                                      ),
                                      child: Column(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        children: [
                                          Row(
                                            children: [
                                              CircleAvatar(
                                                radius: 16.0,
                                                backgroundColor: greyACACAC,
                                                backgroundImage: NetworkImage(
                                                  review.userPictureUrl!,
                                                ),
                                              ),
                                              const SizedBox(width: 12.0),
                                              Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Padding(
                                                    padding:
                                                        const EdgeInsets.only(
                                                            left: 2.0),
                                                    child: Text(
                                                      review.userNickname!,
                                                      style:
                                                          commonSubMediumStyle
                                                              .copyWith(
                                                        fontSize: 14.0,
                                                      ),
                                                    ),
                                                  ),
                                                  const SizedBox(height: 2.0),
                                                  Row(
                                                    mainAxisAlignment:
                                                        MainAxisAlignment.start,
                                                    children: [
                                                      starRatingRow(
                                                        starRating:
                                                            review.starRating!,
                                                        width: 16.0,
                                                        height: 16.0,
                                                      ),
                                                      const SizedBox(
                                                          width: 8.0),
                                                      Text(
                                                        '$year.$month.$day',
                                                        style:
                                                            commonSubRegularStyle
                                                                .copyWith(
                                                          fontSize: 12.0,
                                                          color: grey777777,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                ],
                                              ),
                                            ],
                                          ),
                                          const SizedBox(height: 16.0),
                                          if (review.content!.length < 150)
                                            Text(
                                              review.content!,
                                              style: bookReviewTitleStyle,
                                            )
                                          else ...[
                                            // 더보기 버튼 생성
                                            Text(
                                              '${review.content!.substring(0, 150)}${isExpanded ? review.content!.substring(150) : ''}',
                                              style: bookReviewTitleStyle,
                                            ),
                                            const SizedBox(height: 4.0),
                                            if (!isExpanded)
                                              InkWell(
                                                onTap: () {
                                                  setState(() {
                                                    isExpanded = true;
                                                  });
                                                },
                                                child: Text(
                                                  '더보기 ⬇️',
                                                  style: commonSubMediumStyle
                                                      .copyWith(
                                                    fontSize: 13.0,
                                                  ),
                                                ),
                                              ),
                                            if (isExpanded)
                                              InkWell(
                                                onTap: () {
                                                  setState(() {
                                                    isExpanded = false;
                                                  });
                                                },
                                                child: Text(
                                                  '숨기기 ⬆️',
                                                  style: commonSubMediumStyle
                                                      .copyWith(
                                                    fontSize: 13.0,
                                                  ),
                                                ),
                                              ),
                                          ],
                                        ],
                                      ),
                                    );
                                  },
                                  separatorBuilder: (context, index) {
                                    return _divider();
                                  },
                                ),
                              ),
                            ],
                          )
                        : const DataError(
                            icon: Icons.rate_review_rounded,
                            errorMessage: '아직 작성된 마이 리뷰가 없어요!',
                          );
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

  Widget _divider() {
    return const Divider(
      height: 1,
      thickness: 1,
      color: greyF1F2F5,
    );
  }
}
