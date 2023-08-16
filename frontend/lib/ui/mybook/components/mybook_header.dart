import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class MyBookHeader extends StatelessWidget {
  final List<MyBooksResponseData> myBooksData;
  final List<MyBooksResponseData> completedBooksData;
  final List<BookListResponseData> interestBooksData;
  final GestureTapCallback? onTapInterestBook;
  final void Function({
    required String status,
    required String order,
    required String readStatus,
  })? onTapMyBook;

  const MyBookHeader({
    required this.myBooksData,
    required this.completedBooksData,
    required this.interestBooksData,
    this.onTapInterestBook,
    this.onTapMyBook,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SliverToBoxAdapter(
      child: Container(
        color: greyF1F2F5,
        height: 80.0,
        padding: const EdgeInsets.symmetric(
          horizontal: 32.0,
          vertical: 8.0,
        ),
        child: Container(
          decoration: BoxDecoration(
            color: commonWhiteColor,
            borderRadius: BorderRadius.circular(10.0),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              _headerButton(
                context: context,
                status: '완독북',
                iconUrl:
                    completedBooksData.isEmpty ? 'read.svg' : 'read_green.svg',
                count: '${completedBooksData.length}',
                onTap: () => onTapMyBook!(
                  status: '완독북',
                  order: '',
                  readStatus: 'COMPLETED',
                ),
              ),
              _divider(),
              _headerButton(
                context: context,
                status: '관심북',
                iconUrl:
                    interestBooksData.isEmpty ? 'heart.svg' : 'heart_green.svg',
                count: '${interestBooksData.length}',
                onTap: onTapInterestBook,
              ),
              _divider(),
              _headerButton(
                context: context,
                status: '마이북',
                iconUrl:
                    myBooksData.isEmpty ? 'holder.svg' : 'holder_green.svg',
                count: '${myBooksData.length}',
                onTap: () => onTapMyBook!(
                  status: '마이북',
                  order: '',
                  readStatus: '',
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _headerButton({
    required BuildContext context,
    required String status,
    required String iconUrl,
    required String count,
    GestureTapCallback? onTap,
  }) {
    return InkWell(
      onTap: onTap,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            status,
            style: myBookButtonTitleStyle,
          ),
          const SizedBox(height: 6.0),
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SvgPicture.asset(
                'assets/svg/icon/small/$iconUrl',
                width: 19.0,
                height: 19.0,
              ),
              const SizedBox(width: 8.0),
              Text(
                count,
                style: commonSubBoldStyle.copyWith(
                  height: 1.2,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _divider() {
    return const Padding(
      padding: EdgeInsets.symmetric(horizontal: 22.0),
      child: VerticalDivider(
        width: 20,
        thickness: 1,
        indent: 10,
        endIndent: 10,
        color: greyACACAC,
      ),
    );
  }
}
