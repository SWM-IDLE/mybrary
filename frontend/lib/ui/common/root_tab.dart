import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';

class RootTab extends StatefulWidget {
  const RootTab({super.key});

  @override
  State<RootTab> createState() => _RootTabState();
}

class _RootTabState extends State<RootTab> {
  int index = 0;

  @override
  Widget build(BuildContext context) {
    return BottomNavigationBar(
      selectedItemColor: BLACK_COLOR,
      unselectedItemColor: GREY_03_COLOR,
      selectedFontSize: 12,
      unselectedFontSize: 12,
      type: BottomNavigationBarType.fixed,
      onTap: (int index) {
        setState(() {
          this.index = index;
        });
      },
      currentIndex: index,
      items: [
        BottomNavigationBarItem(
          label: '홈',
          icon: SvgPicture.asset(
            'assets/svg/icon/home.svg',
            colorFilter: ColorFilter.mode(GREY_03_COLOR, BlendMode.srcIn),
          ),
          activeIcon: SvgPicture.asset('assets/svg/icon/home.svg'),
        ),
        BottomNavigationBarItem(
          label: '검색',
          icon: SvgPicture.asset(
            'assets/svg/icon/search.svg',
            colorFilter: ColorFilter.mode(GREY_03_COLOR, BlendMode.srcIn),
          ),
          activeIcon: SvgPicture.asset('assets/svg/icon/search.svg'),
        ),
        BottomNavigationBarItem(
          label: '마이브러리',
          icon: SvgPicture.asset(
            'assets/svg/icon/mybrary.svg',
            colorFilter: ColorFilter.mode(GREY_03_COLOR, BlendMode.srcIn),
          ),
          activeIcon: SvgPicture.asset('assets/svg/icon/mybrary.svg'),
        ),
      ],
    );
  }
}
