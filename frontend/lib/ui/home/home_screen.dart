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
import 'package:mybrary/ui/home/components/home_book_count.dart';
import 'package:mybrary/ui/home/components/home_intro.dart';
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

  @override
  void initState() {
    super.initState();

    _todayRegisteredBookCountData =
        _homeRepository.getTodayRegisteredBookCount();

    _bookListByCategoryData = _homeRepository.getBookListByCategory(
      type: 'Bestseller',
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
                final bookListByCategory =
                    homeData.bookListByCategoryResponseData.books!;

                return SliverToBoxAdapter(
                  child: Column(
                    children: [
                      HomeBookCount(
                        todayRegisteredBookCount: todayRegisteredBookCount!,
                      ),
                      Container(
                        height: 248,
                        padding: const EdgeInsets.symmetric(
                          vertical: 16.0,
                        ),
                        child: Column(
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(
                                  left: 16.0, bottom: 16.0),
                              child: Row(
                                children: [
                                  const Text(
                                    '이번 주',
                                    style: commonMainRegularStyle,
                                  ),
                                  Text(
                                    ' 베스트셀러 ',
                                    style: commonSubBoldStyle.copyWith(
                                      fontSize: 16.0,
                                    ),
                                  ),
                                  const Text(
                                    '는?',
                                    style: commonMainRegularStyle,
                                  ),
                                ],
                              ),
                            ),
                            Expanded(
                              child: ListView.builder(
                                scrollDirection: Axis.horizontal,
                                physics: const BouncingScrollPhysics(),
                                itemCount: bookListByCategory.length,
                                itemBuilder: (context, index) {
                                  return Padding(
                                    padding: const EdgeInsets.only(
                                      right: 10.0,
                                      bottom: 10.0,
                                    ),
                                    child: Row(
                                      children: [
                                        if (index == 0)
                                          const SizedBox(
                                            width: 16.0,
                                          ),
                                        InkWell(
                                          onTap: () {
                                            Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder: (_) =>
                                                    SearchDetailScreen(
                                                  isbn13:
                                                      bookListByCategory[index]
                                                          .isbn13!,
                                                ),
                                              ),
                                            );
                                          },
                                          child: Container(
                                            width: 116,
                                            decoration: BoxDecoration(
                                              image: DecorationImage(
                                                image: NetworkImage(
                                                  bookListByCategory[index]
                                                      .thumbnailUrl!,
                                                ),
                                                fit: BoxFit.fill,
                                              ),
                                              borderRadius:
                                                  BorderRadius.circular(8),
                                              boxShadow: const [
                                                BoxShadow(
                                                  color: Color(0x3F000000),
                                                  blurRadius: 2,
                                                  offset: Offset(1, 1),
                                                  spreadRadius: 1,
                                                )
                                              ],
                                            ),
                                          ),
                                        ),
                                        if (index ==
                                            bookListByCategory.length - 1)
                                          const SizedBox(
                                            width: 8.0,
                                          ),
                                      ],
                                    ),
                                  );
                                },
                              ),
                            ),
                          ],
                        ),
                      ),
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

  List<Future<Object>> _futureHomeData() {
    return [
      _todayRegisteredBookCountData,
      _bookListByCategoryData,
    ];
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

  HomeCommonData _buildHomeData(List<Object> data) {
    final [
      todayRegisteredBookCountResponseData,
      bookListByCategoryResponseData,
    ] = data;
    return HomeCommonData(
      todayRegisteredBookCountResponseData: todayRegisteredBookCountResponseData
          as TodayRegisteredBookCountResponseData,
      bookListByCategoryResponseData:
          bookListByCategoryResponseData as BookListByCategoryResponseData,
    );
  }
}
