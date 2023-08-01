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
import 'package:mybrary/ui/setting/setting_screen.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  late Future<ProfileResponseData> _profileResponseData;
  final _profileRepository = ProfileRepository();

  @override
  void initState() {
    super.initState();
    _profileResponseData = _profileRepository.getProfileData();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: profileAppBar(),
      child: SingleChildScrollView(
        child: FutureBuilder(
          future: _profileResponseData,
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
                        onTapWriteIntroduction: onTapWriteIntroduction,
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
                return SingleChildScrollView(
                  child: _profileMenuBottomSheet(),
                );
              },
            );
          },
          icon: SvgPicture.asset('assets/svg/icon/menu.svg'),
        ),
      ],
    );
  }

  Widget _profileMenuBottomSheet() {
    return SizedBox(
      height: 220,
      child: Padding(
        padding: const EdgeInsets.symmetric(
          horizontal: 28.0,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            profileMenuTab('👤  프로필 편집', const ProfileEditScreen()),
            profileMenuTab('🔗️️  설정', const SettingScreen()),
            const SizedBox(height: 12.0),
            const Padding(
              padding: EdgeInsets.only(left: 8.0),
              child: Text(
                '🔖  프로필 공유하기',
                style: bottomSheetMenuTextStyle,
              ),
            ),
            const SizedBox(height: 18.0),
          ],
        ),
      ),
    );
  }

  Widget profileMenuTab(String tabText, Widget screen) {
    return Container(
      padding: const EdgeInsets.only(bottom: 12.0),
      child: TextButton(
        style: commonMenuButtonStyle,
        onPressed: () {
          onPressedProfileMenu(screen);
        },
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              tabText,
              style: bottomSheetMenuTextStyle,
            ),
            SvgPicture.asset(
              'assets/svg/icon/profile_menu_arrow.svg',
            ),
          ],
        ),
      ),
    );
  }

  void onTapWriteIntroduction() {
    _navigateToNextScreen(const ProfileEditScreen());
  }

  void onPressedProfileMenu(Widget screen) {
    Navigator.pop(context);
    _navigateToNextScreen(screen);
  }

  void _navigateToNextScreen(Widget screen) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => screen),
    ).then(
      (value) => setState(() {
        _profileResponseData = _profileRepository.getProfileData();
      }),
    );
  }
}
