import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/mybook/mybook_list/mybook_list_screen.dart';

class MyBookHeader extends StatelessWidget {
  final List<MyBooksResponseData> myBooksData;
  final List<BookListResponseData> interestBooksData;
  final GestureTapCallback? onTap;

  const MyBookHeader({
    required this.myBooksData,
    required this.interestBooksData,
    this.onTap,
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
                iconUrl: 'read.svg',
                count: '0',
                bookList: [],
              ),
              _divider(),
              _headerButton(
                context: context,
                status: '관심북',
                iconUrl:
                    interestBooksData.isEmpty ? 'heart.svg' : 'heart_green.svg',
                count: '${interestBooksData.length}',
                onTap: onTap,
              ),
              _divider(),
              _headerButton(
                context: context,
                status: '마이북',
                iconUrl:
                    myBooksData.isEmpty ? 'holder.svg' : 'holder_green.svg',
                count: '${myBooksData.length}',
                bookList: myBooksData,
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
    List<MyBooksResponseData>? bookList,
  }) {
    return InkWell(
      onTap: onTap ??
          () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => MyBookListScreen(
                  bookListTitle: status,
                  bookList: bookList!,
                ),
              ),
            );
          },
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
