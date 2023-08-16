import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/home/today_registered_book_count_response.dart';
import 'package:mybrary/data/repository/home_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/components/home_barcode_button.dart';
import 'package:mybrary/ui/home/components/home_book_count.dart';
import 'package:mybrary/ui/home/components/home_intro.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _homeRepository = HomeRepository();

  late Future<TodayRegisteredBookCountResponseData>
      _getTodayRegisteredBookCount;

  @override
  void initState() {
    super.initState();

    _getTodayRegisteredBookCount =
        _homeRepository.getTodayRegisteredBookCount();
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
          FutureBuilder<TodayRegisteredBookCountResponseData>(
            future: _getTodayRegisteredBookCount,
            builder: (context, snapshot) {
              if (snapshot.hasError) {
                return const HomeBookCount(
                  todayRegisteredBookCount: 0,
                );
              }

              if (snapshot.hasData) {
                final todayRegisteredBookCount = snapshot.data!.count;
                return HomeBookCount(
                  todayRegisteredBookCount: todayRegisteredBookCount!,
                );
              }
              return const HomeBookCount(
                todayRegisteredBookCount: 0,
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
}
