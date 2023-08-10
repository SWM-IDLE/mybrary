import 'package:flutter/material.dart';
import 'package:mybrary/data/model/book/book_list_response.dart';
import 'package:mybrary/data/model/book/my_book_common_data.dart';
import 'package:mybrary/data/model/book/my_books_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/mybook/components/mybook_header.dart';

class MyBookScreen extends StatefulWidget {
  const MyBookScreen({super.key});

  @override
  State<MyBookScreen> createState() => _MyBookScreenState();
}

class _MyBookScreenState extends State<MyBookScreen> {
  final _profileRepository = ProfileRepository();
  final _bookRepository = BookRepository();

  late Future<ProfileResponseData> _profileResponseData;
  late Future<List<MyBooksResponseData>> _myBooksResponseData;
  late Future<List<BookListResponseData>> _interestBooksResponseData;

  @override
  void initState() {
    super.initState();

    _profileResponseData = _profileRepository.getProfileData();
    _myBooksResponseData = _bookRepository.getMyBooks(
      userId: 'testId',
    );
    _interestBooksResponseData = _bookRepository.getInterestBooks(
      userId: 'testId',
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
          if (snapshot.hasData) {
            MyBookCommonData data = snapshot.data!;
            ProfileResponseData profileData = data.profileResponseData;
            List<MyBooksResponseData> myBooksData = data.myBooksResponseData;
            List<BookListResponseData> interestBooksData =
                data.interestBooksResponseData;

            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                _myBookAppBar(profileData),
                MyBookHeader(
                  myBooksData: myBooksData,
                  interestBooksData: interestBooksData,
                ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  SliverAppBar _myBookAppBar(ProfileResponseData profileData) {
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
            backgroundImage: NetworkImage(
              profileData.profileImageUrl!,
            ),
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
      centerTitle: false,
      foregroundColor: commonBlackColor,
    );
  }

  List<Future<Object>> _futureMyBookData() {
    return [
      _profileResponseData,
      _myBooksResponseData,
      _interestBooksResponseData,
    ];
  }

  MyBookCommonData _futureMyBookResponseData(List<Object> data) {
    final [
      profileResponseData,
      myBooksResponseData,
      interestBooksResponseData
    ] = data;
    return MyBookCommonData(
      profileResponseData: profileResponseData as ProfileResponseData,
      myBooksResponseData: myBooksResponseData as List<MyBooksResponseData>,
      interestBooksResponseData:
          interestBooksResponseData as List<BookListResponseData>,
    );
  }
}
