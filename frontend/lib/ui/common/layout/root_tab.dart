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
    'label': '마이북',
    'iconPath': 'assets/svg/icon/mybrary.svg',
  },
  {
    'label': '프로필',
    'iconPath': 'assets/svg/icon/profile.svg',
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
      length: 4,
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
          Center(
            child: Text('마이북'),
          ),
          MybraryScreen(),
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: BLACK_COLOR,
        unselectedItemColor: GREY_03_COLOR,
        selectedFontSize: 12,
        selectedLabelStyle: TextStyle(fontWeight: FontWeight.w700),
        unselectedFontSize: 12,
        type: BottomNavigationBarType.fixed,
        onTap: (int index) {
          tabController.animateTo(index);
        },
        currentIndex: index,
        items: bottomNavigationBarItemList
            .map((e) => BottomNavigationBarItem(
                  label: e['label'],
                  icon: Column(
                    children: [
                      SvgPicture.asset(
                        e['iconPath']!,
                        colorFilter:
                            ColorFilter.mode(GREY_03_COLOR, BlendMode.srcIn),
                      ),
                      SizedBox(height: 4.0),
                    ],
                  ),
                  activeIcon: Column(
                    children: [
                      SvgPicture.asset(e['iconPath']!),
                      SizedBox(height: 4.0),
                    ],
                  ),
                ))
            .toList(),
      ),
    );
  }
}
