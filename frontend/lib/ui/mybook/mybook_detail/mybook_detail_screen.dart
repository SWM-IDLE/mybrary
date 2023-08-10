import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/book/mybook_detail_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_detail_header.dart';
import 'package:mybrary/ui/mybook/mybook_detail/components/mybook_record.dart';

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

  late Future<MyBookDetailResponseData> _myBookDetail;

  @override
  void initState() {
    super.initState();

    _myBookDetail = _bookRepository.getMyBookDetail(
      userId: 'testId',
      myBookId: widget.myBookId,
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: FutureBuilder<MyBookDetailResponseData>(
        future: _myBookDetail,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            print(snapshot.error);
            return const Center(
              child: Text('마이북 도서 데이터를 불러오는데 실패했습니다.'),
            );
          }

          if (snapshot.hasData) {
            final myBookDetailData = snapshot.data!;

            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                _myBookDetailAppBar(''),
                MyBookDetailHeader(
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
                const SliverToBoxAdapter(
                  child: SizedBox(height: 50.0),
                ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  SliverAppBar _myBookDetailAppBar(String appBarTitle) {
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
}
