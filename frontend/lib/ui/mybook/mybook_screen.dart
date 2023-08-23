import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/mybook_common_data.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/mybook/components/mybook_header.dart';
import 'package:mybrary/ui/mybook/interest_book_list/interest_book_list_screen.dart';
import 'package:mybrary/ui/mybook/mybook_list/mybook_list_screen.dart';
import 'package:mybrary/utils/logics/ui_utils.dart';

class MyBookScreen extends StatefulWidget {
  final String? userId;

  const MyBookScreen({
    this.userId,
    super.key,
  });

  @override
  State<MyBookScreen> createState() => _MyBookScreenState();
}

class _MyBookScreenState extends State<MyBookScreen> {
  final _profileRepository = ProfileRepository();
  final _bookRepository = BookRepository();

  late Future<ProfileResponseData> _profileResponseData;
  late Future<ProfileImageResponseData> _profileImageResponseData;
  late Future<List<MyBooksResponseData>> _myBooksResponseData;
  late Future<List<MyBooksResponseData>> _completedBooksResponseData;
  late Future<List<BookListResponseData>> _interestBooksResponseData;

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _profileResponseData = _profileRepository.getProfileData(
      context: context,
      userId: widget.userId ?? _userId,
    );
    _profileImageResponseData = _profileRepository.getProfileImage(
      context: context,
      userId: widget.userId ?? _userId,
    );
    _myBooksResponseData = _bookRepository.getMyBooks(
      context: context,
      userId: widget.userId ?? _userId,
      order: '',
      readStatus: '',
    );
    _completedBooksResponseData = _bookRepository.getMyBooks(
      context: context,
      userId: widget.userId ?? _userId,
      order: '',
      readStatus: 'COMPLETED',
    );
    _interestBooksResponseData = _bookRepository.getInterestBooks(
      context: context,
      userId: widget.userId ?? _userId,
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: FutureBuilder<MyBookCommonData>(
        future: Future.wait(_futureMyBookData()).then(
          (data) => _futureMyBookResponseData(data),
        ),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return const CustomScrollView(
              physics: BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                SliverToBoxAdapter(
                  child: SizedBox(
                    height: 80.0,
                  ),
                ),
                SliverToBoxAdapter(
                  child: DataError(
                    errorMessage: '마이북을 불러오는데 실패했습니다.',
                  ),
                )
              ],
            );
          }

          if (snapshot.hasData) {
            MyBookCommonData data = snapshot.data!;
            ProfileResponseData profileData = data.profileResponseData;
            ProfileImageResponseData profileImageData =
                data.profileImageResponseData;
            List<MyBooksResponseData> myBooksData = data.myBooksResponseData;
            List<MyBooksResponseData> completedBooksData =
                data.completedBooksResponseData;
            List<BookListResponseData> interestBooksData =
                data.interestBooksResponseData;

            final myBooksBookShelfData = _limitedBookShelfData(myBooksData);
            final completedBooksBookShelfData =
                _limitedBookShelfData(completedBooksData);
            final interestBooksBookShelfData =
                _limitedBookShelfData(interestBooksData);

            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                _myBookAppBar(
                  profileData: profileData,
                  imageUrl: profileImageData.profileImageUrl!,
                ),
                MyBookHeader(
                  myBooksData: myBooksData,
                  completedBooksData: completedBooksData,
                  interestBooksData: interestBooksData,
                  onTapInterestBook: _refreshInterestBookScreen,
                  onTapMyBook: _refreshMyBookScreen,
                ),
                SliverPadding(
                  padding: const EdgeInsets.all(20.0),
                  sliver: SliverToBoxAdapter(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          '마이 서재',
                          style: commonSubTitleStyle,
                        ),
                        _myBookShelfItem(
                          myBooksBookShelfData: myBooksBookShelfData,
                          status: '마이북',
                          order: '',
                          readStatus: '',
                        ),
                        _myBookShelfItem(
                          myBooksBookShelfData: interestBooksBookShelfData,
                          status: '관심북',
                          order: '',
                          readStatus: '',
                        ),
                        _myBookShelfItem(
                          myBooksBookShelfData: completedBooksBookShelfData,
                          status: '완독북',
                          order: '',
                          readStatus: 'COMPLETED',
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  List<dynamic> _limitedBookShelfData(List<dynamic> myBooksData) {
    return myBooksData.length > 5
        ? myBooksData.reversed.toList().sublist(0, 5)
        : myBooksData.reversed.toList();
  }

  Padding _myBookShelfItem({
    required List<dynamic> myBooksBookShelfData,
    required String status,
    required String order,
    required String readStatus,
  }) {
    final leftPosition = mediaQueryWidth(context) / 6.2;
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: InkWell(
        onTap: status == '관심북'
            ? _refreshInterestBookScreen
            : () => _refreshMyBookScreen(
                  status: status,
                  order: order,
                  readStatus: readStatus,
                ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Row(
                  children: [
                    SvgPicture.asset(
                      myBooksBookShelfData.isEmpty
                          ? 'assets/svg/icon/small/holder.svg'
                          : 'assets/svg/icon/small/holder_green.svg',
                      width: 14.0,
                      height: 14.0,
                    ),
                    const SizedBox(width: 4.0),
                    Text(
                      status,
                      style: bookShelfTitleStyle,
                    ),
                  ],
                ),
                SizedBox(
                  child: SvgPicture.asset(
                    'assets/svg/icon/right_arrow.svg',
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8.0),
            Container(
              width: double.infinity,
              height: 150.0,
              decoration: BoxDecoration(
                color: greyDDDDDD,
                borderRadius: BorderRadius.circular(8.0),
                boxShadow: [
                  BoxShadow(
                    color: commonBlackColor.withOpacity(0.3),
                    blurRadius: 2,
                    offset: const Offset(0, 1),
                    spreadRadius: 1,
                  )
                ],
              ),
              child: myBooksBookShelfData.isEmpty
                  ? Center(
                      child: Text(
                        '내가 설정한 책이 자동으로 담깁니다.',
                        style: bookShelfTitleStyle.copyWith(
                          color: grey999999,
                        ),
                      ),
                    )
                  : Stack(
                      children: myBooksBookShelfData
                          .map(
                            (myBook) => Positioned(
                              left: myBooksBookShelfData.indexOf(myBook) *
                                  leftPosition,
                              child: Container(
                                width: 100.0,
                                height: 140.0,
                                decoration: ShapeDecoration(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(8.0),
                                  ),
                                  image: DecorationImage(
                                    image: NetworkImage(
                                      status == '관심북'
                                          ? myBook.thumbnailUrl!
                                          : myBook.book!.thumbnailUrl!,
                                    ),
                                    fit: BoxFit.fill,
                                  ),
                                ),
                              ),
                            ),
                          )
                          .toList()
                          .reversed
                          .toList(),
                    ),
            ),
          ],
        ),
      ),
    );
  }

  SliverAppBar _myBookAppBar({
    required ProfileResponseData profileData,
    required String imageUrl,
  }) {
    return SliverAppBar(
      toolbarHeight: 80.0,
      backgroundColor: commonWhiteColor,
      elevation: 0,
      pinned: true,
      title: Row(
        children: [
          CircleAvatar(
            radius: 20.0,
            backgroundColor: greyACACAC,
            backgroundImage: NetworkImage(imageUrl),
          ),
          const SizedBox(
            width: 12.0,
          ),
          Flexible(
            child: Text.rich(
              maxLines: 2,
              TextSpan(
                text: profileData.nickname!,
                style: myBookAppBarTextStyle.copyWith(
                  fontWeight: FontWeight.w700,
                ),
                children: const [
                  TextSpan(
                    text: '의 마이브러리',
                    style: myBookAppBarTextStyle,
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
      titleTextStyle: appBarTitleStyle,
      centerTitle: widget.userId == null ? false : true,
      foregroundColor: commonBlackColor,
    );
  }

  List<Future<Object>> _futureMyBookData() {
    return [
      _profileResponseData,
      _profileImageResponseData,
      _myBooksResponseData,
      _completedBooksResponseData,
      _interestBooksResponseData,
    ];
  }

  MyBookCommonData _futureMyBookResponseData(List<Object> data) {
    final [
      profileResponseData,
      profileImageResponseData,
      myBooksResponseData,
      completedBooksResponseData,
      interestBooksResponseData
    ] = data;
    return MyBookCommonData(
      profileResponseData: profileResponseData as ProfileResponseData,
      profileImageResponseData:
          profileImageResponseData as ProfileImageResponseData,
      myBooksResponseData: myBooksResponseData as List<MyBooksResponseData>,
      completedBooksResponseData:
          completedBooksResponseData as List<MyBooksResponseData>,
      interestBooksResponseData:
          interestBooksResponseData as List<BookListResponseData>,
    );
  }

  void _refreshInterestBookScreen() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => InterestBookListScreen(
          userId: widget.userId,
        ),
      ),
    ).then(
      (value) => setState(() {
        _interestBooksResponseData = _bookRepository.getInterestBooks(
          context: context,
          userId: widget.userId ?? _userId,
        );
        _myBooksResponseData = _bookRepository.getMyBooks(
          context: context,
          userId: widget.userId ?? _userId,
          order: '',
          readStatus: '',
        );
      }),
    );
  }

  void _refreshMyBookScreen({
    required String status,
    required String order,
    required String readStatus,
  }) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => MyBookListScreen(
          bookListTitle: status,
          order: order,
          readStatus: readStatus,
          userId: widget.userId,
        ),
      ),
    ).then(
      (value) => setState(() {
        if (readStatus == 'COMPLETED') {
          _completedBooksResponseData = _bookRepository.getMyBooks(
            context: context,
            userId: widget.userId ?? _userId,
            order: order,
            readStatus: readStatus,
          );
          _myBooksResponseData = _bookRepository.getMyBooks(
            context: context,
            userId: widget.userId ?? _userId,
            order: '',
            readStatus: '',
          );
          _interestBooksResponseData = _bookRepository.getInterestBooks(
            context: context,
            userId: widget.userId ?? _userId,
          );
        }
        if (readStatus == '') {
          _myBooksResponseData = _bookRepository.getMyBooks(
            context: context,
            userId: widget.userId ?? _userId,
            order: order,
            readStatus: readStatus,
          );
          _completedBooksResponseData = _bookRepository.getMyBooks(
            context: context,
            userId: widget.userId ?? _userId,
            order: '',
            readStatus: 'COMPLETED',
          );
          _interestBooksResponseData = _bookRepository.getInterestBooks(
            context: context,
            userId: widget.userId ?? _userId,
          );
        }
      }),
    );
  }
}
