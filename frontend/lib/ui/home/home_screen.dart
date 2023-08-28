import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/book_recommendations_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/repository/home_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/components/home_barcode_button.dart';
import 'package:mybrary/ui/home/components/home_best_seller.dart';
import 'package:mybrary/ui/home/components/home_book_count.dart';
import 'package:mybrary/ui/home/components/home_intro.dart';
import 'package:mybrary/ui/home/components/home_recommend_books.dart';
import 'package:mybrary/ui/profile/my_interests/my_interests_screen.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _homeRepository = HomeRepository();

  late Future<TodayRegisteredBookCountResponseData>
      _todayRegisteredBookCountData;

  late Future<BookListByCategoryResponseData> _bookListByCategoryData;
  late Future<BookRecommendationsResponseData> _bookRecommendationsData;

  late String _bookCategory = '';
  late List<BookRecommendations> _bookListByCategory = [];

  final ScrollController _categoryScrollController = ScrollController();

  final _userId = UserState.userId;

  @override
  void initState() {
    super.initState();

    _homeRepository
        .getBookListByInterest(
      context: context,
      type: 'Bestseller',
      userId: _userId,
    )
        .then(
      (data) {
        if (data.userInterests!.isNotEmpty) {
          _bookCategory = data.userInterests![0].name!;
        }
      },
    );

    _todayRegisteredBookCountData = _homeRepository.getTodayRegisteredBookCount(
      context: context,
    );
    _bookListByCategoryData = _homeRepository.getBookListByCategory(
      context: context,
      type: 'Bestseller',
    );
    _bookRecommendationsData = _homeRepository.getBookListByInterest(
      context: context,
      type: 'Bestseller',
      userId: _userId,
    );
  }

  void _scrollToTop() {
    _categoryScrollController.animateTo(
      0,
      duration: const Duration(microseconds: 500),
      curve: Curves.easeInOut,
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: CustomScrollView(
        physics: const BouncingScrollPhysics(
          parent: AlwaysScrollableScrollPhysics(),
        ),
        slivers: [
          _homeAppBar(),
          const HomeIntro(),
          const HomeBarcodeButton(),
          SliverToBoxAdapter(
            child: FutureBuilder<TodayRegisteredBookCountResponseData>(
                future: _todayRegisteredBookCountData,
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return buildErrorPage();
                  }

                  if (snapshot.hasData) {
                    return HomeBookCount(
                      todayRegisteredBookCount: snapshot.data!.count!,
                    );
                  }
                  return const CircularLoading();
                }),
          ),
          SliverToBoxAdapter(
            child: FutureBuilder<BookListByCategoryResponseData>(
                future: _bookListByCategoryData,
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return buildErrorPage();
                  }
                  if (snapshot.hasData) {
                    return Container(
                      height: 258,
                      padding: const EdgeInsets.symmetric(
                        vertical: 16.0,
                      ),
                      child: HomeBestSeller(
                        bookListByBestSeller: snapshot.data!.books!,
                        onTapBook: (String isbn13) {
                          _navigateToBookSearchDetailScreen(isbn13);
                        },
                      ),
                    );
                  }
                  return Container();
                }),
          ),
          SliverToBoxAdapter(
            child: FutureBuilder<BookRecommendationsResponseData>(
                future: _bookRecommendationsData,
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return buildErrorPage();
                  }

                  if (snapshot.hasData) {
                    final result = snapshot.data!;
                    final interests = _getUserInterests(result.userInterests);
                    final [firstInterest, secondInterest, thirdInterest] =
                        interests;

                    if (_bookListByCategory.isEmpty) {
                      _bookListByCategory
                          .addAll([...result.bookRecommendations!]);
                    }

                    return Container(
                      height: 310,
                      padding: const EdgeInsets.symmetric(
                        vertical: 16.0,
                      ),
                      child: HomeRecommendBooks(
                          category: _bookCategory,
                          userInterests: result.userInterests!,
                          bookListByCategory: _bookListByCategory,
                          categoryScrollController: _categoryScrollController,
                          onTapBook: (String isbn13) {
                            _navigateToBookSearchDetailScreen(isbn13);
                          },
                          onTapMyInterests: _navigateToMyInterestsScreen,
                          onTapCategory: (String category) {
                            setState(() {
                              _bookCategory = category;
                              _setInterests(firstInterest, category);
                              _setInterests(secondInterest, category);
                              _setInterests(thirdInterest, category);
                            });
                          }),
                    );
                  }
                  return Container();
                }),
          ),
          const SliverToBoxAdapter(
            child: SizedBox(
              height: 30.0,
            ),
          )
        ],
      ),
    );
  }

  SliverAppBar _homeAppBar() {
    return SliverAppBar(
      toolbarHeight: 70.0,
      backgroundColor: commonWhiteColor,
      elevation: 0,
      pinned: true,
      title: SvgPicture.asset('assets/svg/icon/home_logo.svg'),
      titleTextStyle: appBarTitleStyle,
      centerTitle: false,
      foregroundColor: commonBlackColor,
    );
  }

  void _navigateToBookSearchDetailScreen(String isbn13) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => SearchDetailScreen(
          isbn13: isbn13,
        ),
      ),
    ).then((value) => {
          setState(() {
            _todayRegisteredBookCountData =
                _homeRepository.getTodayRegisteredBookCount(
              context: context,
            );
          })
        });
  }

  void _navigateToMyInterestsScreen() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => const MyInterestsScreen(),
      ),
    ).then(
      (value) => setState(() {
        _homeRepository
            .getBookListByInterest(
              context: context,
              type: 'Bestseller',
              userId: _userId,
            )
            .then(
              (data) => _bookCategory = data.userInterests![0].name!,
            );
        _bookRecommendationsData = _homeRepository.getBookListByInterest(
          context: context,
          type: 'Bestseller',
          userId: _userId,
        );
      }),
    );
  }

  void _refreshBookLists(UserInterests interests) async {
    await _homeRepository
        .getBookListByCategory(
          context: context,
          type: 'Bestseller',
          categoryId: interests.code!,
        )
        .then(
          (data) => setState(() {
            _bookListByCategory = data.books!;
          }),
        );
  }

  List<UserInterests> _getUserInterests(List<UserInterests>? userInterests) {
    List<UserInterests> interests = userInterests ?? [];
    List<UserInterests> assignedInterests = List.filled(
        3,
        UserInterests.fromJson(
          UserInterests().toJson(),
        ));

    for (int i = 0; i < interests.length && i < 3; i++) {
      assignedInterests[i] = interests[i];
    }

    return assignedInterests;
  }

  void _setInterests(UserInterests interest, String category) {
    if (interest.name != null && category == interest.name!) {
      _refreshBookLists(interest);
      _scrollToTop();
    }
  }
}
