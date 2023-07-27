import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/mybrary/components/my_profile.dart';
import 'package:mybrary/ui/mybrary/my_intro/my_intro_screen.dart';

class MybraryScreen extends StatefulWidget {
  const MybraryScreen({super.key});

  @override
  State<MybraryScreen> createState() => _MybraryScreenState();
}

class _MybraryScreenState extends State<MybraryScreen>
    with SingleTickerProviderStateMixin {
  late TabController _mybraryTabController;

  @override
  void initState() {
    super.initState();

    _mybraryTabController = TabController(length: 5, vsync: this);
  }

  @override
  void dispose() {
    super.dispose();
    _mybraryTabController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: mybraryAppBar(),
      child: SafeArea(
        child: Column(
          children: [
            MyProfile(),
            SizedBox(height: 10.0),
            Padding(
              padding: const EdgeInsets.symmetric(
                horizontal: 24.0,
                vertical: 16.0,
              ),
              child: MyIntroScreen(),
            ),
          ],
        ),
      ),
    );
  }

  AppBar mybraryAppBar() {
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
