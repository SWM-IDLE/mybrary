import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';

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
    final followTextStyle = TextStyle(
      fontSize: 14.0,
      fontWeight: FontWeight.w400,
    );

    return DefaultLayout(
      appBar: AppBar(
        backgroundColor: WHITE_COLOR,
        elevation: 0,
        title: Text('마이브러리'),
        titleTextStyle: TextStyle(
          color: BLACK_COLOR,
          fontSize: 22,
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
      ),
      child: SingleChildScrollView(
        child: SafeArea(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 16.0,
                  vertical: 20.0,
                ),
                child: Row(
                  children: [
                    Container(
                      width: 82.0,
                      height: 82.0,
                      decoration: ShapeDecoration(
                        shape: RoundedRectangleBorder(
                          side: BorderSide(width: 2, color: BOOK_BORDER_COLOR),
                          borderRadius: BorderRadius.circular(50),
                        ),
                        image: DecorationImage(
                          image: NetworkImage(
                            // 프로필 임시 이미지
                            'https://blog.kakaocdn.net/dn/SDhEI/btqZWOAubQF/cNfyvunWb9cKg7DlaPE9mK/img.jpg',
                          ),
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    SizedBox(width: 12.0),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          '박보영',
                          style: TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                        SizedBox(
                          height: 4.0,
                        ),
                        Wrap(
                          spacing: 5,
                          children: [
                            Text('팔로워', style: followTextStyle),
                            Text('264', style: followTextStyle),
                            SizedBox(width: 6.0),
                            Text('팔로잉', style: followTextStyle),
                            Text('123', style: followTextStyle),
                          ],
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              SizedBox(height: 20.0),
              Container(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height,
                decoration: ShapeDecoration(
                  color: WHITE_COLOR,
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
                    Container(
                      width: MediaQuery.of(context).size.width,
                      height: MediaQuery.of(context).size.height * 0.70,
                      margin: EdgeInsets.all(16.0),
                      child: TabBarView(
                        controller: _mybraryTabController,
                        children: [
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                '한 줄 소개',
                                style: TextStyle(
                                  fontSize: 16.0,
                                  fontWeight: FontWeight.w700,
                                ),
                              ),
                              SizedBox(height: 14.0),
                              Text(
                                '마이브러리는 도서의 가치를 발견하고,\n사람을 잇는 서비스입니다.',
                                style: TextStyle(
                                  color: GREY_05_COLOR,
                                  fontSize: 13.0,
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                              SizedBox(height: 28.0),
                              Text(
                                '마이 관심사',
                                style: TextStyle(
                                  fontSize: 16.0,
                                  fontWeight: FontWeight.w700,
                                ),
                              ),
                              SizedBox(height: 14.0),
                              Text(
                                '인문학, 스릴러, IT/프로그래밍',
                                style: TextStyle(
                                  color: GREY_05_COLOR,
                                  fontSize: 13.0,
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                              SizedBox(height: 28.0),
                              Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                crossAxisAlignment: CrossAxisAlignment.center,
                                children: [
                                  Text(
                                    '마이 뱃지',
                                    style: TextStyle(
                                      fontSize: 16.0,
                                      fontWeight: FontWeight.w700,
                                    ),
                                  ),
                                  IconButton(
                                    onPressed: () {},
                                    icon: Icon(
                                      Icons.keyboard_arrow_right_outlined,
                                      color: GREY_06_COLOR,
                                      size: 26.0,
                                    ),
                                  ),
                                ],
                              ),
                              SizedBox(height: 14.0),
                              Container(
                                width: MediaQuery.of(context).size.width * 0.9,
                                height: 72.0,
                                child: ListView.builder(
                                  scrollDirection: Axis.horizontal,
                                  itemCount: 4,
                                  itemBuilder: (context, index) {
                                    final iconPath = [
                                      'assets/svg/badge/beginner_reviewer.svg',
                                      'assets/svg/badge/my_member.svg',
                                      'assets/svg/badge/read_through.svg',
                                      'assets/svg/badge/influencer.svg',
                                    ];

                                    return Container(
                                      width: 72.0,
                                      height: 72.0,
                                      margin: EdgeInsets.only(right: 10.0),
                                      decoration: ShapeDecoration(
                                        shape: RoundedRectangleBorder(
                                          side: BorderSide(
                                              width: 1,
                                              color: BOOK_BORDER_COLOR),
                                          borderRadius:
                                              BorderRadius.circular(50),
                                        ),
                                      ),
                                      child: SvgPicture.asset(
                                        iconPath[index],
                                        fit: BoxFit.cover,
                                      ),
                                    );
                                  },
                                ),
                              ),
                            ],
                          ),
                          Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color: Colors.orangeAccent,
                            ),
                          ),
                          Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color: Colors.greenAccent,
                            ),
                          ),
                          Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color: Colors.indigoAccent,
                            ),
                          ),
                          Container(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color: Colors.redAccent,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
