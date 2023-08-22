import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_common_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/follow_repository.dart';
import 'package:mybrary/data/repository/interests_repository.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/enum.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/profile/components/profile_header.dart';
import 'package:mybrary/ui/profile/components/profile_intro.dart';
import 'package:mybrary/ui/profile/follow/follow_screen.dart';
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
  final _followRepository = FollowRepository();

  late Future<ProfileResponseData> _profileResponseData;
  late Future<MyInterestsResponseData> _myInterestsResponseData;
  late Future<FollowerResponseData> _followerResponseData;
  late Future<FollowingResponseData> _followingResponseData;
  late Future<ProfileImageResponseData> _profileImageResponseData;
  late List<UserInterests> userInterests;
  late String _userId;

  @override
  void initState() {
    super.initState();

    _userId = UserState.userId;

    _profileResponseData = _profileRepository.getProfileData(
      context: context,
      userId: _userId,
    );
    _profileImageResponseData = _profileRepository.getProfileImage(
      context: context,
      userId: _userId,
    );
    _myInterestsResponseData = _myInterestsRepository.getMyInterestsCategories(
      context: context,
      userId: _userId,
    );
    _followerResponseData = _followRepository.getFollower(
      context: context,
      userId: _userId,
    );
    _followingResponseData = _followRepository.getFollowings(
      context: context,
      userId: _userId,
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      appBar: _profileAppBar(),
      child: SingleChildScrollView(
        physics: const BouncingScrollPhysics(),
        child: FutureBuilder<ProfileCommonData>(
          future: Future.wait(_futureProfileData())
              .then((data) => _buildProfileData(data)),
          builder: (context, snapshot) {
            if (snapshot.hasError) {
              return const SingleDataError(
                errorMessage: '프로필을 불러오는데 실패했습니다.',
              );
            }

            if (snapshot.hasData) {
              ProfileCommonData data = snapshot.data!;
              final ProfileResponseData profileData = data.profileData;
              final MyInterestsResponseData myInterestsData =
                  data.myInterestsData;
              final FollowerResponseData followerData = data.followerData;
              final FollowingResponseData followingData = data.followingData;
              ProfileImageResponseData profileImageData = data.profileImageData;

              return Column(
                children: [
                  ProfileHeader(
                    nickname: profileData.nickname!,
                    profileImageUrl: profileImageData.profileImageUrl!,
                    followerCount: followerData.followers!.length.toString(),
                    followingCount: followingData.followings!.length.toString(),
                    navigateToFollowScreen: () =>
                        _navigateToFollowerScreen(profileData.nickname!),
                    navigateToFollowingScreen: () =>
                        _navigateToFollowingScreen(profileData.nickname!),
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

  ProfileCommonData _buildProfileData(List<Object> data) {
    final [
      profileData,
      profileImageData,
      myInterestsData,
      followerData,
      followingData
    ] = data;
    return ProfileCommonData(
      profileData: profileData as ProfileResponseData,
      profileImageData: profileImageData as ProfileImageResponseData,
      myInterestsData: myInterestsData as MyInterestsResponseData,
      followerData: followerData as FollowerResponseData,
      followingData: followingData as FollowingResponseData,
    );
  }

  List<Future<Object>> _futureProfileData() {
    return [
      _profileResponseData,
      _profileImageResponseData,
      _myInterestsResponseData,
      _followerResponseData,
      _followingResponseData,
    ];
  }

  AppBar _profileAppBar() {
    return AppBar(
      toolbarHeight: 70.0,
      backgroundColor: commonWhiteColor,
      elevation: 0,
      title: const Text('프로필'),
      titleTextStyle: appBarTitleStyle,
      centerTitle: false,
      foregroundColor: commonBlackColor,
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
    return Padding(
      padding: const EdgeInsets.symmetric(
        horizontal: 28.0,
        vertical: 32.0,
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _profileMenuTab('👤  프로필 편집', const ProfileEditScreen()),
          _profileMenuTab('🔗️️  설정', const SettingScreen()),
        ],
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
        _profileResponseData = _profileRepository.getProfileData(
          context: context,
          userId: _userId,
        );
        _profileImageResponseData = _profileRepository.getProfileImage(
          context: context,
          userId: _userId,
        );
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
          context: context,
          userId: _userId,
        );
      }),
    );
  }

  void _navigateToFollowerScreen(String nickname) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => FollowScreen(
          nickname: nickname,
          pageType: FollowPageType.follower,
        ),
      ),
    ).then(
      (value) => setState(() {
        _followerResponseData = _followRepository.getFollower(
          context: context,
          userId: _userId,
        );
        _followingResponseData = _followRepository.getFollowings(
          context: context,
          userId: _userId,
        );
      }),
    );
  }

  void _navigateToFollowingScreen(String nickname) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => FollowScreen(
          nickname: nickname,
          pageType: FollowPageType.following,
        ),
      ),
    ).then(
      (value) => setState(() {
        _followerResponseData = _followRepository.getFollower(
          context: context,
          userId: _userId,
        );
        _followingResponseData = _followRepository.getFollowings(
          context: context,
          userId: _userId,
        );
      }),
    );
  }
}
