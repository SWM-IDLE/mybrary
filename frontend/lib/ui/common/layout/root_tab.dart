import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/home/home_screen.dart';
import 'package:mybrary/ui/mybook/mybook_screen.dart';
import 'package:mybrary/ui/profile/profile_screen.dart';
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
  final int? tapIndex;

  const RootTab({
    this.tapIndex,
    super.key,
  });

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

    if (widget.tapIndex != null) {
      index = widget.tapIndex!;
      tabController.index = widget.tapIndex!;
    }

    tabController.addListener(tabListener);
  }

  void tabListener() {
    setState(() {
      index = tabController.index;
    });
  }

  @override
  void dispose() {
    tabController.removeListener(tabListener);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      bottomNavigationBar: Container(
        decoration: const BoxDecoration(
          border: Border(
            top: BorderSide(
              color: greyF1F2F5,
              width: 1.0,
            ),
          ),
        ),
        child: BottomNavigationBar(
          elevation: 0,
          backgroundColor: commonWhiteColor,
          selectedItemColor: commonBlackColor,
          unselectedItemColor: greyACACAC,
          selectedFontSize: 12,
          selectedLabelStyle: const TextStyle(
            fontWeight: FontWeight.w700,
          ),
          unselectedFontSize: 12,
          type: BottomNavigationBarType.fixed,
          onTap: (int index) {
            tabController.animateTo(index);
          },
          currentIndex: index,
          items: bottomNavigationBarItemList
              .map(
                (e) => BottomNavigationBarItem(
                  label: e['label'],
                  icon: Column(
                    children: [
                      SvgPicture.asset(
                        e['iconPath']!,
                        colorFilter: const ColorFilter.mode(
                          greyACACAC,
                          BlendMode.srcIn,
                        ),
                      ),
                      const SizedBox(height: 4.0),
                    ],
                  ),
                  activeIcon: Column(
                    children: [
                      SvgPicture.asset(e['iconPath']!),
                      const SizedBox(height: 4.0),
                    ],
                  ),
                ),
              )
              .toList(),
        ),
      ),
      child: TabBarView(
        physics: const NeverScrollableScrollPhysics(),
        controller: tabController,
        children: const [
          HomeScreen(),
          SearchScreen(),
          MyBookScreen(),
          ProfileScreen(),
        ],
      ),
    );
  }
}
