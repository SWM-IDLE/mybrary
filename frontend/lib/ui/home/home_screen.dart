import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/home/book_list_by_category_response.dart';
import 'package:mybrary/data/model/home/home_common_response.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/repository/home_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/components/home_barcode_button.dart';
import 'package:mybrary/ui/home/components/home_best_seller.dart';
import 'package:mybrary/ui/home/components/home_book_count.dart';
import 'package:mybrary/ui/home/components/home_intro.dart';
import 'package:mybrary/ui/home/components/home_recommend_books.dart';
import 'package:mybrary/ui/search/search_detail/search_detail_screen.dart';

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

  @override
  void initState() {
    super.initState();

    _todayRegisteredBookCountData =
        _homeRepository.getTodayRegisteredBookCount();

    _bookListByCategoryData = _homeRepository.getBookListByCategory(
      type: 'Bestseller',
    );

    // 홈 화면 카테고리 별 추천 도서 임시 데이터
    _bookListByGenreNovelData = _homeRepository.getBookListByCategory(
      type: 'Bestseller',
      categoryId: 112011,
    );
    _bookListByPsychologyData = _homeRepository.getBookListByCategory(
      type: 'Bestseller',
      categoryId: 51395,
    );
    _bookListByTravelData = _homeRepository.getBookListByCategory(
      type: 'Bestseller',
      categoryId: 1196,
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
          FutureBuilder<HomeCommonData>(
            future: Future.wait(_futureHomeData())
                .then((data) => _buildHomeData(data)),
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
                        errorMessage: '메인 화면을 불러오는데 실패했습니다.',
                      ),
                    )
                  ],
                );
              }

              if (snapshot.hasData) {
                HomeCommonData homeData = snapshot.data!;
                final todayRegisteredBookCount =
                    homeData.todayRegisteredBookCountResponseData.count;
                final bookListByBestSeller =
                    homeData.bookListByCategoryResponseData.books!;
                final bookListByGenreNovel =
                    homeData.bookListByGenreNovelData.books!;
                final bookListByPsychology =
                    homeData.bookListByPsychologyData.books!;
                final bookListByTravel = homeData.bookListByTravelData.books!;

                if (_bookListByCategory.isEmpty) {
                  _bookListByCategory.addAll([...bookListByGenreNovel]);
                }

                return SliverToBoxAdapter(
                  child: Column(
                    children: [
                      HomeBookCount(
                        todayRegisteredBookCount: todayRegisteredBookCount!,
                      ),
                      Container(
                        height: 258,
                        padding: const EdgeInsets.symmetric(
                          vertical: 16.0,
                        ),
                        child: HomeBestSeller(
                          bookListByBestSeller: bookListByBestSeller,
                          onTapBook: (String isbn13) {
                            _nextToBookSearchDetailScreen(isbn13);
                          },
                        ),
                      ),
                      Container(
                        height: 310,
                        padding: const EdgeInsets.symmetric(
                          vertical: 16.0,
                        ),
                        child: HomeRecommendBooks(
                            category: _bookCategory,
                            bookListByCategory: _bookListByCategory,
                            onTapBook: (String isbn13) {
                              _nextToBookSearchDetailScreen(isbn13);
                            },
                            onTapCategory: (String category) {
                              setState(() {
                                _bookCategory = category;
                                switch (category) {
                                  case '심리학':
                                    _bookListByCategory = bookListByPsychology;
                                    break;
                                  case '여행':
                                    _bookListByCategory = bookListByTravel;
                                    break;
                                  default:
                                    _bookListByCategory = bookListByGenreNovel;
                                }
                              });
                            }),
                      ),
                      const SizedBox(height: 30.0),
                    ],
                  ),
                );
              }
              return const SliverToBoxAdapter(
                child: CircularLoading(),
              );
            },
          ),
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
      _todayRegisteredBookCountData,
      _bookListByCategoryData,
      _bookListByGenreNovelData,
      _bookListByPsychologyData,
      _bookListByTravelData
    ];
  }

  HomeCommonData _buildHomeData(List<Object> data) {
    final [
      todayRegisteredBookCountResponseData,
      bookListByCategoryResponseData,
      bookListByGenreNovelData,
      bookListByPsychologyData,
      bookListByTravelData,
    ] = data;
    return HomeCommonData(
      todayRegisteredBookCountResponseData: todayRegisteredBookCountResponseData
          as TodayRegisteredBookCountResponseData,
      bookListByCategoryResponseData:
          bookListByCategoryResponseData as BookListByCategoryResponseData,
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
                _homeRepository.getTodayRegisteredBookCount();
          })
        });
  }
}
