import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_common_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/interests_repository.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/profile/components/profile_header.dart';
import 'package:mybrary/ui/profile/components/profile_intro.dart';
import 'package:mybrary/ui/profile/my_interests/my_interests_screen.dart';
import 'package:mybrary/ui/profile/profile_edit/profile_edit_screen.dart';
import 'package:mybrary/ui/setting/setting_screen.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final _profileRepository = ProfileRepository();
  final _myInterestsRepository = InterestsRepository();

  late Future<ProfileResponseData> _profileResponseData;
  late Future<MyInterestsResponseData> _myInterestsResponseData;

  late List<UserInterests> userInterests;

  @override
  void initState() {
    super.initState();
    _profileResponseData = _profileRepository.getProfileData();
    _myInterestsResponseData = _myInterestsRepository.getMyInterestsCategories(
      userId: 'testId',
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: _profileAppBar(),
      child: SingleChildScrollView(
        physics: const BouncingScrollPhysics(),
        child: FutureBuilder(
          future: Future.wait([_profileResponseData, _myInterestsResponseData])
              .then((data) => ProfileCommonData(
                  profileData: data[0] as ProfileResponseData,
                  myInterestsData: data[1] as MyInterestsResponseData)),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              ProfileCommonData data = snapshot.data!;
              final ProfileResponseData profileData = data.profileData;
              final MyInterestsResponseData myInterestsData =
                  data.myInterestsData;

              return Column(
                children: [
                  ProfileHeader(
                    nickname: profileData.nickname!,
                    profileImageUrl: profileData.profileImageUrl!,
                  ),
                  ProfileIntro(
                    introduction: profileData.introduction!,
                    userInterests: myInterestsData.userInterests!,
                    onTapWriteIntroduction: _onTapWriteIntroduction,
                    onTapMyInterests: () =>
                        _onTapMyInterests(myInterestsData.userInterests!),
                  ),
                ],
              );
            }
            return const CircularLoading();
          },
        ),
      ),
    );
  }

  List<Future<Object>> futureProfileData() {
    return [
      _profileResponseData,
      _myInterestsResponseData,
    ];
  }

  AppBar _profileAppBar() {
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
            _profileMenuTab('👤  프로필 편집', const ProfileEditScreen()),
            _profileMenuTab('🔗️️  설정', const SettingScreen()),
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

  Widget _profileMenuTab(String tabText, Widget screen) {
    return Container(
      padding: const EdgeInsets.only(bottom: 12.0),
      child: TextButton(
        style: commonMenuButtonStyle,
        onPressed: () {
          _onPressedProfileMenu(screen);
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

  void _onTapWriteIntroduction() {
    _navigateToProfileEditScreen(const ProfileEditScreen());
  }

  void _onTapMyInterests(List<UserInterests> userInterests) {
    _navigateToMyInterestsScreen(
      MyInterestsScreen(userInterests: userInterests),
    );
  }

  void _onPressedProfileMenu(Widget screen) {
    Navigator.pop(context);
    _navigateToProfileEditScreen(screen);
  }

  void _navigateToProfileEditScreen(Widget screen) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => screen),
    ).then(
      (value) => setState(() {
        _profileResponseData = _profileRepository.getProfileData();
      }),
    );
  }

  void _navigateToMyInterestsScreen(Widget screen) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => screen),
    ).then(
      (value) => setState(() {
        _myInterestsResponseData =
            _myInterestsRepository.getMyInterestsCategories(
          userId: 'testId',
        );
      }),
    );
  }
}
