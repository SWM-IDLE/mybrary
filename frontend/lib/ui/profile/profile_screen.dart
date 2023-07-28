import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/profile/components/profile_header.dart';
import 'package:mybrary/ui/profile/components/profile_intro.dart';
import 'package:mybrary/ui/profile/profile_edit/profile_edit_screen.dart';
import 'package:mybrary/utils/animation/route_animation.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen>
    with SingleTickerProviderStateMixin {
  late TabController _profileTabController;

  @override
  void initState() {
    super.initState();

    _profileTabController = TabController(length: 5, vsync: this);
  }

  @override
  void dispose() {
    super.dispose();
    _profileTabController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: profileAppBar(),
      child: SafeArea(
        child: Column(
          children: [
            ProfileHeader(),
            Padding(
              padding: const EdgeInsets.only(
                left: 20.0,
                top: 24.0,
                right: 4.0,
                bottom: 24.0,
              ),
              child: ProfileIntro(),
            ),
          ],
        ),
      ),
    );
  }

  AppBar profileAppBar() {
    return AppBar(
      toolbarHeight: 70.0,
      backgroundColor: WHITE_COLOR,
      elevation: 0,
      title: Text('프로필'),
      titleTextStyle: TextStyle(
        color: BLACK_COLOR,
        fontSize: 21,
        fontWeight: FontWeight.w700,
      ),
      centerTitle: false,
      foregroundColor: BLACK_COLOR,
      actions: [
        IconButton(
          onPressed: () {
            showModalBottomSheet(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(20.0),
                  topRight: Radius.circular(20.0),
                ),
              ),
              backgroundColor: Colors.white,
              context: context,
              builder: (_) {
                return profileMenu();
              },
            );
          },
          icon: SvgPicture.asset('assets/svg/icon/menu.svg'),
        ),
      ],
    );
  }

  Widget profileMenu() {
    return Container(
      height: 280,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 28.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            profileMenuTab('👤 프로필 편집', ProfileEditScreen()),
            SizedBox(height: 28.0),
            profileMenuTab('📈 독서 리포트', Container()),
            SizedBox(height: 28.0),
            profileMenuTab('🏫 소속 인증', Container()),
            SizedBox(height: 28.0),
            profileMenuTab('🔗️️ 설정', Container()),
            SizedBox(height: 20.0),
            Text(
              '🔖 프로필 공유하기',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w400,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget profileMenuTab(String tabText, Widget screen) {
    final menuTextStyle = TextStyle(
      height: 1.0,
      fontSize: 16.0,
      fontWeight: FontWeight.w400,
    );

    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: () {
        RouteAnimation routeAnimation = RouteAnimation(screen);
        Navigator.pop(context);
        Navigator.push(context, routeAnimation.slideRightToLeft());
      },
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            tabText,
            style: menuTextStyle,
          ),
          SvgPicture.asset('assets/svg/icon/profile_menu_arrow.svg'),
        ],
      ),
    );
  }
}
