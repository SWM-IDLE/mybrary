import 'package:flutter/material.dart';
import 'package:mybrary/data/model/search/book_detail_review_response.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/utils/logics/book_utils.dart';

class ReviewItem extends StatefulWidget {
  final MyBookReviewList review;
  final GestureTapCallback? onTapDelete;

  const ReviewItem({
    required this.review,
    required this.onTapDelete,
    super.key,
  });

  @override
  State<ReviewItem> createState() => _ReviewItemState();
}

class _ReviewItemState extends State<ReviewItem> {
  bool _isTextOverflow = false;
  bool _isMore = false;

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();
    _isTextOverflow = widget.review.content!.length > 150;
    _isMore = !_isTextOverflow;
  }

  @override
  Widget build(BuildContext context) {
    List<String> publicationDate =
        widget.review.createdAt!.substring(0, 10).split('-');
    final [year, month, day] = publicationDate;

    return Padding(
      padding: const EdgeInsets.symmetric(
        horizontal: 16.0,
        vertical: 4.0,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  CircleAvatar(
                    radius: 16.0,
                    backgroundColor: greyACACAC,
                    backgroundImage: NetworkImage(
                      widget.review.userPictureUrl!,
                    ),
                  ),
                  const SizedBox(width: 12.0),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 2.0),
                        child: Text(
                          widget.review.userNickname!,
                          style: commonSubMediumStyle.copyWith(
                            fontSize: 14.0,
                          ),
                        ),
                      ),
                      const SizedBox(height: 2.0),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: [
                          starRatingRow(
                            starRating: widget.review.starRating!,
                            width: 16.0,
                            height: 16.0,
                          ),
                          const SizedBox(width: 8.0),
                          Text(
                            '$year.$month.$day',
                            style: commonSubRegularStyle.copyWith(
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
              if (_userId == widget.review.userId)
                InkWell(
                  onTap: () {
                    widget.onTapDelete!();
                  },
                  child: Text(
                    '삭제',
                    style: commonSubThinStyle.copyWith(
                      color: commonRedColor,
                    ),
                  ),
                ),
            ],
          ),
          const SizedBox(height: 16.0),
          Text(
            widget.review.content!,
            style: bookReviewTitleStyle,
            maxLines: _isTextOverflow && !_isMore ? 5 : null,
            overflow: _isTextOverflow && !_isMore
                ? TextOverflow.ellipsis
                : TextOverflow.visible,
          ),
          const SizedBox(height: 4.0),
          if (_isTextOverflow && !_isMore)
            Align(
              alignment: Alignment.centerRight,
              child: InkWell(
                onTap: () {
                  setState(() {
                    _isMore = true;
                  });
                },
                child: Padding(
                  padding: const EdgeInsets.all(4.0),
                  child: Text(
                    '더보기',
                    style: commonSubMediumStyle.copyWith(
                      fontSize: 13.0,
                    ),
                  ),
                ),
              ),
            ),
          if (_isTextOverflow && _isMore)
            Align(
              alignment: Alignment.centerRight,
              child: InkWell(
                onTap: () {
                  setState(() {
                    _isMore = false;
                  });
                },
                child: Text(
                  '숨기기',
                  style: commonSubMediumStyle.copyWith(
                    fontSize: 13.0,
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }
}
