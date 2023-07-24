import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/home_screen.dart';
import 'package:mybrary/ui/mybrary/mybrary_screen.dart';
import 'package:mybrary/ui/search/search_screen.dart';

const bottomNavigationBarItemList = [
  {
    'label': '홈',
    'iconPath': 'assets/svg/icon/home.svg',
  },
  {
    'label': '검색',
    'iconPath': 'assets/svg/icon/search.svg',
  },
  {
    'label': '마이브러리',
    'iconPath': 'assets/svg/icon/mybrary.svg',
  },
];

class RootTab extends StatefulWidget {
  const RootTab({super.key});

  @override
  State<RootTab> createState() => _RootTabState();
}

class _RootTabState extends State<RootTab> with SingleTickerProviderStateMixin {
  late TabController tabController;

  int index = 0;

  @override
  void initState() {
    super.initState();

    tabController = TabController(
      length: 3,
      vsync: this,
      animationDuration: Duration.zero,
    );

    tabController.addListener(tabListener);
  }

  @override
  void dispose() {
    tabController.removeListener(tabListener);
    super.dispose();
  }

  void tabListener() {
    setState(() {
      index = tabController.index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: TabBarView(
        physics: NeverScrollableScrollPhysics(),
        controller: tabController,
        children: [
          HomeScreen(),
          SearchScreen(),
          MybraryScreen(),
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: BLACK_COLOR,
        unselectedItemColor: GREY_03_COLOR,
        selectedFontSize: 12,
        unselectedFontSize: 12,
        type: BottomNavigationBarType.fixed,
        onTap: (int index) {
          tabController.animateTo(index);
        },
        currentIndex: index,
        items: bottomNavigationBarItemList
            .map((e) => BottomNavigationBarItem(
                  label: e['label'],
                  icon: SvgPicture.asset(
                    e['iconPath']!,
                    colorFilter:
                        ColorFilter.mode(GREY_03_COLOR, BlendMode.srcIn),
                  ),
                  activeIcon: SvgPicture.asset(e['iconPath']!),
                ))
            .toList(),
      ),
    );
  }
}
