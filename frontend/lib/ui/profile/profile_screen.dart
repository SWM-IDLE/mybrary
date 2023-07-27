import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/profile/components/profile_header.dart';
import 'package:mybrary/ui/profile/components/profile_intro.dart';

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
          onPressed: () {},
          icon: SvgPicture.asset('assets/svg/icon/menu.svg'),
        ),
      ],
    );
  }
}
