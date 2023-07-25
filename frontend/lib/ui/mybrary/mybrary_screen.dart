import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/mybrary/components/my_profile.dart';
import 'package:mybrary/ui/mybrary/my_book/my_book_screen.dart';
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
    return Container(
      decoration: BoxDecoration(
        image: DecorationImage(
          alignment: Alignment.topLeft,
          image: AssetImage('assets/img/test/mylogo.png'),
        ),
      ),
      child: DefaultLayout(
        backgroundColor: Colors.transparent,
        appBar: mybraryAppBar(),
        child: SafeArea(
          child: Column(
            children: [
              MyProfile(),
              SizedBox(height: 20.0),
              Expanded(
                child: Container(
                  decoration: ShapeDecoration(
                    color: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(15),
                        topRight: Radius.circular(15),
                      ),
                    ),
                    shadows: [
                      BoxShadow(
                        color: Color(0x26000000),
                        blurRadius: 4,
                        offset: Offset(0, -4),
                        spreadRadius: 0,
                      )
                    ],
                  ),
                  child: Column(
                    children: [
                      TabBar(
                        padding: EdgeInsets.only(
                          left: 10.0,
                          top: 4.0,
                          bottom: 4.0,
                          right: 22.0,
                        ),
                        controller: _mybraryTabController,
                        indicatorColor: BLACK_COLOR,
                        labelColor: BLACK_COLOR,
                        labelStyle: TextStyle(
                          fontSize: 14.0,
                          fontWeight: FontWeight.w700,
                        ),
                        unselectedLabelColor: GREY_04_COLOR,
                        unselectedLabelStyle: TextStyle(
                          fontSize: 14.0,
                          fontWeight: FontWeight.w500,
                        ),
                        isScrollable: true,
                        tabs: [
                          Tab(
                            text: "소개",
                          ),
                          Tab(
                            text: "마이북",
                          ),
                          Tab(
                            text: "책장",
                          ),
                          Tab(
                            text: "북노트",
                          ),
                          Tab(
                            text: "독서 리포트",
                          ),
                        ],
                      ),
                      Expanded(
                        child: SizedBox(
                          height: MediaQuery.of(context).size.height,
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: TabBarView(
                              controller: _mybraryTabController,
                              children: [
                                MyIntroScreen(),
                                MyBookScreen(),
                                Center(
                                  child: Text('책장'),
                                ),
                                Center(
                                  child: Text('북노트'),
                                ),
                                Center(
                                  child: Text('독서 리포트'),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  AppBar mybraryAppBar() {
    return AppBar(
      backgroundColor: Colors.transparent,
      elevation: 0,
      title: Text('마이브러리'),
      titleTextStyle: TextStyle(
        color: BLACK_COLOR,
        fontSize: 20,
        fontWeight: FontWeight.w700,
      ),
      centerTitle: false,
      foregroundColor: BLACK_COLOR,
      actions: [
        Wrap(
          spacing: -10,
          children: [
            IconButton(
              onPressed: () {},
              icon: SvgPicture.asset('assets/svg/icon/write.svg'),
            ),
            IconButton(
              onPressed: () {},
              icon: SvgPicture.asset('assets/svg/icon/menu.svg'),
            ),
          ],
        ),
      ],
    );
  }
}
