import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
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

class _ProfileScreenState extends State<ProfileScreen> {
  late Future<ProfileResponseData> _profileData;
  final _profileRepository = ProfileRepository();

  @override
  void initState() {
    super.initState();
    _profileData = _profileRepository.getProfileData();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: profileAppBar(),
      child: SingleChildScrollView(
        child: FutureBuilder(
          future: _profileData,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final profileData = snapshot.data!;

              return SizedBox(
                height: MediaQuery.of(context).size.height,
                child: Column(
                  children: [
                    ProfileHeader(
                      nickname: profileData.nickname!,
                      profileImageUrl: profileData.profileImageUrl!,
                    ),
                    Padding(
                      padding: const EdgeInsets.only(
                        left: 20.0,
                        top: 24.0,
                        right: 4.0,
                        bottom: 24.0,
                      ),
                      child: ProfileIntro(
                        introduction: profileData.introduction!,
                      ),
                    ),
                  ],
                ),
              );
            }
            return const CircularLoading();
          },
        ),
      ),
    );
  }

  AppBar profileAppBar() {
    return AppBar(
      toolbarHeight: 70.0,
      backgroundColor: WHITE_COLOR,
      elevation: 0,
      title: const Text('프로필'),
      titleTextStyle: appBarTitleStyle,
      centerTitle: false,
      foregroundColor: BLACK_COLOR,
      actions: [
        IconButton(
          onPressed: () {
            showModalBottomSheet(
              shape: bottomSheetStyle,
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
      height: 200,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 28.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            profileMenuTab('👤  프로필 편집', ProfileEditScreen()),
            SizedBox(height: 36.0),
            profileMenuTab('🔗️️  설정', Container()),
            SizedBox(height: 28.0),
            Text(
              '🔖  프로필 공유하기',
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
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: () {
        RouteAnimation routeAnimation = RouteAnimation(screen);
        Navigator.pop(context);
        Navigator.push(
          context,
          routeAnimation.slideRightToLeft(),
        ).then(
          (value) => setState(
            () {
              _profileData = _profileRepository.getProfileData();
            },
          ),
        );
      },
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Text(
            tabText,
            style: bottomSheetMenuTextStyle,
          ),
          SvgPicture.asset('assets/svg/icon/profile_menu_arrow.svg'),
        ],
      ),
    );
  }
}
