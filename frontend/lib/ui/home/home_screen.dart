import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/home_common_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/repository/home_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/components/home_barcode_button.dart';
import 'package:mybrary/ui/home/components/home_best_seller.dart';
import 'package:mybrary/ui/home/components/home_book_count.dart';
import 'package:mybrary/ui/home/components/home_intro.dart';
import 'package:mybrary/ui/home/components/home_recommend_books.dart';
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
  late Future<BookListByCategoryResponseData> _bookListByGenreNovelData;
  late Future<BookListByCategoryResponseData> _bookListByPsychologyData;
  late Future<BookListByCategoryResponseData> _bookListByTravelData;

  late String _bookCategory = '장르소설';
  late List<Books> _bookListByCategory = [];

  final ScrollController _categoryScrollController = ScrollController();

  @override
  void initState() {
    super.initState();

    _todayRegisteredBookCountData = _homeRepository.getTodayRegisteredBookCount(
      context: context,
    );

    _bookListByCategoryData = _homeRepository.getBookListByCategory(
      context: context,
      type: 'Bestseller',
    );

    // 홈 화면 카테고리 별 추천 도서 임시 데이터
    _bookListByGenreNovelData = _homeRepository.getBookListByCategory(
      context: context,
      type: 'Bestseller',
      categoryId: 112011,
    );
    _bookListByPsychologyData = _homeRepository.getBookListByCategory(
      context: context,
      type: 'Bestseller',
      categoryId: 51395,
    );
    _bookListByTravelData = _homeRepository.getBookListByCategory(
      context: context,
      type: 'Bestseller',
      categoryId: 1196,
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
                          _nextToBookSearchDetailScreen(isbn13);
                        },
                      ),
                    );
                  }
                  return Container();
                }),
          ),
          SliverToBoxAdapter(
            child: FutureBuilder<HomeCommonData>(
                future: Future.wait(_futureHomeData())
                    .then((data) => _buildHomeData(data)),
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return buildErrorPage();
                  }

                  if (snapshot.hasData) {
                    final data = snapshot.data!;
                    final bookListByGenreNovel =
                        data.bookListByGenreNovelData.books!;
                    final bookListByPsychology =
                        data.bookListByPsychologyData.books!;
                    final bookListByTravel = data.bookListByTravelData.books!;

                    if (_bookListByCategory.isEmpty) {
                      _bookListByCategory.addAll([...bookListByGenreNovel]);
                    }

                    return Container(
                      height: 310,
                      padding: const EdgeInsets.symmetric(
                        vertical: 16.0,
                      ),
                      child: HomeRecommendBooks(
                          category: _bookCategory,
                          bookListByCategory: _bookListByCategory,
                          categoryScrollController: _categoryScrollController,
                          onTapBook: (String isbn13) {
                            _nextToBookSearchDetailScreen(isbn13);
                          },
                          onTapCategory: (String category) {
                            setState(() {
                              _bookCategory = category;
                              switch (category) {
                                case '심리학':
                                  _bookListByCategory = bookListByPsychology;
                                  _scrollToTop();
                                  break;
                                case '여행':
                                  _bookListByCategory = bookListByTravel;
                                  _scrollToTop();
                                  break;
                                default:
                                  _bookListByCategory = bookListByGenreNovel;
                                  _scrollToTop();
                              }
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

  List<Future<Object>> _futureHomeData() {
    return [
      _bookListByGenreNovelData,
      _bookListByPsychologyData,
      _bookListByTravelData
    ];
  }

  HomeCommonData _buildHomeData(List<Object> data) {
    final [
      bookListByGenreNovelData,
      bookListByPsychologyData,
      bookListByTravelData,
    ] = data;
    return HomeCommonData(
      bookListByGenreNovelData:
          bookListByGenreNovelData as BookListByCategoryResponseData,
      bookListByPsychologyData:
          bookListByPsychologyData as BookListByCategoryResponseData,
      bookListByTravelData:
          bookListByTravelData as BookListByCategoryResponseData,
    );
  }

  void _nextToBookSearchDetailScreen(String isbn13) {
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
}
