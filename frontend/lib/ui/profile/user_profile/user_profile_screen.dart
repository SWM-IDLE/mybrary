import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/data/model/book/mybooks_response.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/data/model/profile/profile_common_response.dart';
import 'package:mybrary/data/model/profile/profile_image_response.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/book_repository.dart';
import 'package:mybrary/data/repository/follow_repository.dart';
import 'package:mybrary/data/repository/interests_repository.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/enum.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';
import 'package:mybrary/ui/mybook/mybook_screen.dart';
import 'package:mybrary/ui/profile/components/profile_header.dart';
import 'package:mybrary/ui/profile/follow/follow_screen.dart';
import 'package:mybrary/ui/profile/user_profile/components/user_profile_intro.dart';
import 'package:mybrary/utils/logics/common_utils.dart';
import 'package:mybrary/utils/logics/ui_utils.dart';

class UserProfileScreen extends StatefulWidget {
  final String userId;
  final String nickname;

  const UserProfileScreen({
    required this.userId,
    required this.nickname,
    super.key,
  });

  @override
  State<UserProfileScreen> createState() => _UserProfileScreenState();
}

class _UserProfileScreenState extends State<UserProfileScreen> {
  final _profileRepository = ProfileRepository();
  final _myInterestsRepository = InterestsRepository();
  final _followRepository = FollowRepository();
  final _bookRepository = BookRepository();

  late Future<ProfileResponseData> _profileResponseData;
  late Future<MyInterestsResponseData> _myInterestsResponseData;
  late Future<FollowerResponseData> _followerResponseData;
  late Future<FollowingResponseData> _followingResponseData;
  late Future<ProfileImageResponseData> _profileImageResponseData;
  late Future<List<MyBooksResponseData>> _userMyBooksResponseData;

  Set<String> notFollowingUsers = {};

  late bool _isFollowing = false;

  @override
  void initState() {
    super.initState();

    _profileResponseData = _profileRepository.getProfileData(
      context: context,
      userId: widget.userId,
    );
    _profileImageResponseData = _profileRepository.getProfileImage(
      context: context,
      userId: widget.userId,
    );
    _myInterestsResponseData = _myInterestsRepository.getMyInterestsCategories(
      context: context,
      userId: widget.userId,
    );
    _followerResponseData = _followRepository.getFollower(
      context: context,
      userId: widget.userId,
    );
    _followingResponseData = _followRepository.getFollowings(
      context: context,
      userId: widget.userId,
    );
    _userMyBooksResponseData = _bookRepository.getMyBooks(
      context: context,
      userId: widget.userId,
      order: '',
      readStatus: '',
    );

    _followRepository
        .getUserFollowStatus(
          context: context,
          userId: widget.userId,
          targetId: widget.userId,
        )
        .then(
          (data) => _isFollowing = data.following!,
        );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: FutureBuilder<UserProfileCommonData>(
        future: Future.wait(_futureUserProfileData())
            .then((data) => _buildUserProfileData(data)),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return const SingleDataError(
              errorMessage: '사용자를 불러오는데 실패했습니다.',
            );
          }
          if (snapshot.hasData) {
            UserProfileCommonData data = snapshot.data!;
            final ProfileResponseData profileData = data.profileData;
            final MyInterestsResponseData myInterestsData =
                data.myInterestsData;
            final FollowerResponseData followerData = data.followerData;
            final FollowingResponseData followingData = data.followingData;
            final ProfileImageResponseData profileImageData =
                data.profileImageData;
            final List<MyBooksResponseData> userMyBooksData = data.myBooksData;

            return CustomScrollView(
              physics: const BouncingScrollPhysics(
                parent: AlwaysScrollableScrollPhysics(),
              ),
              slivers: [
                _userProfileAppBar(),
                SliverToBoxAdapter(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      ProfileHeader(
                        nickname: profileData.nickname!,
                        profileImageUrl: profileImageData.profileImageUrl!,
                        followerCount:
                            followerData.followers!.length.toString(),
                        followingCount:
                            followingData.followings!.length.toString(),
                        navigateToFollowScreen: () =>
                            _navigateToFollowerScreen(profileData.nickname!),
                        navigateToFollowingScreen: () =>
                            _navigateToFollowingScreen(profileData.nickname!),
                      ),
                      UserProfileIntro(
                        introduction: profileData.introduction!,
                        userInterests: myInterestsData.userInterests!,
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 20.0,
                          vertical: 8.0,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              '마이브러리',
                              style: commonSubTitleStyle,
                            ),
                            _myBookOfUser(
                              myBooksBookShelfData: userMyBooksData,
                              status: '마이북',
                              order: '',
                              readStatus: '',
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            );
          }
          return const CircularLoading();
        },
      ),
    );
  }

  SliverAppBar _userProfileAppBar() {
    return SliverAppBar(
      toolbarHeight: 70.0,
      backgroundColor: commonWhiteColor,
      elevation: 0,
      title: Text(widget.nickname),
      titleTextStyle: appBarTitleStyle.copyWith(
        fontSize: 16.0,
      ),
      centerTitle: true,
      foregroundColor: commonBlackColor,
      actions: [
        Padding(
          padding: const EdgeInsets.symmetric(
            horizontal: 12.0,
            vertical: 16.0,
          ),
          child: ElevatedButton(
            onPressed: () => onPressedAddOrDeleteFollowingUser(
              followingUserId: widget.userId,
            ),
            style: ElevatedButton.styleFrom(
              elevation: 0,
              backgroundColor: _isFollowing ? greyDDDDDD : primaryColor,
              shape: followButtonRoundStyle,
              splashFactory: NoSplash.splashFactory,
              padding: const EdgeInsets.symmetric(
                horizontal: 12.0,
                vertical: 4.0,
              ),
            ),
            child: Text(
              _isFollowing ? '팔로잉' : '팔로우',
              style: followButtonTextStyle.copyWith(
                color: _isFollowing ? commonBlackColor : commonWhiteColor,
              ),
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(right: 8.0),
          child: InkWell(
            onTap: () {
              showModalBottomSheet(
                shape: bottomSheetStyle,
                backgroundColor: Colors.white,
                context: context,
                builder: (_) {
                  return SingleChildScrollView(
                    child: _userProfileMenu(),
                  );
                },
              );
            },
            child: const Icon(
              Icons.more_vert_rounded,
            ),
          ),
        ),
      ],
    );
  }

  UserProfileCommonData _buildUserProfileData(List<Object> data) {
    final [
      profileData,
      profileImageData,
      myInterestsData,
      followerData,
      followingData,
      userMyBooksData,
    ] = data;
    return UserProfileCommonData(
      profileData: profileData as ProfileResponseData,
      profileImageData: profileImageData as ProfileImageResponseData,
      myInterestsData: myInterestsData as MyInterestsResponseData,
      followerData: followerData as FollowerResponseData,
      followingData: followingData as FollowingResponseData,
      myBooksData: userMyBooksData as List<MyBooksResponseData>,
    );
  }

  List<Future<Object>> _futureUserProfileData() {
    return [
      _profileResponseData,
      _profileImageResponseData,
      _myInterestsResponseData,
      _followerResponseData,
      _followingResponseData,
      _userMyBooksResponseData,
    ];
  }

  void _navigateToFollowerScreen(String nickname) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => FollowScreen(
          nickname: nickname,
          pageType: FollowPageType.follower,
          userId: widget.userId,
        ),
      ),
    ).then(
      (value) => setState(() {
        _followerResponseData = _followRepository.getFollower(
          context: context,
          userId: widget.userId,
        );
        _followingResponseData = _followRepository.getFollowings(
          context: context,
          userId: widget.userId,
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
          userId: widget.userId,
        ),
      ),
    ).then(
      (value) => setState(() {
        _followerResponseData = _followRepository.getFollower(
          context: context,
          userId: widget.userId,
        );
        _followingResponseData = _followRepository.getFollowings(
          context: context,
          userId: widget.userId,
        );
      }),
    );
  }

  void onPressedDeleteFollowerUser({
    required BuildContext context,
    required Followers follower,
    required List<Followers> followers,
    required int index,
  }) async {
    await _followRepository.deleteFollower(
      context: context,
      userId: widget.userId,
      sourceId: follower.userId!,
    );

    if (!mounted) return;
    showFollowButtonMessage(
      context: context,
      message: '삭제중',
    );

    Future.delayed(const Duration(seconds: 1), () {
      setState(() {
        followers.removeAt(index);
      });
      Navigator.of(context).pop();
    });
  }

  void onPressedAddOrDeleteFollowingUser({
    required String followingUserId,
  }) async {
    if (_isFollowing) {
      await _followRepository.deleteFollowing(
        context: context,
        userId: widget.userId,
        targetId: followingUserId,
      );
      setState(() {
        _isFollowing = false;
      });
      if (!mounted) return;
      showInterestBookMessage(
        context: context,
        snackBarText: '팔로우를 취소했어요.',
      );
    } else {
      await _followRepository.updateFollowing(
        context: context,
        userId: widget.userId,
        targetId: followingUserId,
      );
      setState(() {
        _isFollowing = true;
      });
      if (!mounted) return;
      showInterestBookMessage(
        context: context,
        snackBarText: '사용자를 팔로우했어요.',
      );
    }
  }

  Widget _userProfileMenu() {
    return SizedBox(
      height: 120,
      child: Padding(
        padding: const EdgeInsets.symmetric(
          horizontal: 28.0,
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            InkWell(
              onTap: () async {
                await showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return AlertDialog(
                      title: const Text(
                        '사용자 신고',
                        style: commonSubBoldStyle,
                        textAlign: TextAlign.center,
                      ),
                      content: const Text(
                        '정말 사용자를 신고하시겠습니까?',
                        style: confirmButtonTextStyle,
                        textAlign: TextAlign.center,
                      ),
                      contentPadding: const EdgeInsets.only(
                        top: 24.0,
                        bottom: 16.0,
                      ),
                      actionsAlignment: MainAxisAlignment.center,
                      buttonPadding:
                          const EdgeInsets.symmetric(horizontal: 8.0),
                      actions: [
                        Row(
                          children: [
                            confirmButton(
                              onTap: () {
                                Navigator.of(context).pop();
                                Navigator.of(context).pop();
                              },
                              buttonText: '취소',
                              isCancel: true,
                            ),
                            confirmButton(
                              onTap: () {
                                showInterestBookMessage(
                                  context: context,
                                  snackBarText: '신고가 정상적으로 접수되었습니다.',
                                );
                                Navigator.of(context).pop();
                                Navigator.of(context).pop();
                              },
                              buttonText: '신고하기',
                              isCancel: false,
                            ),
                          ],
                        ),
                      ],
                    );
                  },
                );
              },
              child: const Padding(
                padding: EdgeInsets.only(left: 8.0),
                child: Text(
                  '🚨  사용자 신고하기',
                  style: bottomSheetMenuTextStyle,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _myBookOfUser({
    required List<MyBooksResponseData> myBooksBookShelfData,
    required String status,
    required String order,
    required String readStatus,
  }) {
    final leftPosition = mediaQueryWidth(context) / 6.2;
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: InkWell(
        onTap: () {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (_) => MyBookScreen(
                userId: widget.userId,
              ),
            ),
          );
        },
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Row(
                  children: [
                    SvgPicture.asset(
                      myBooksBookShelfData.isEmpty
                          ? 'assets/svg/icon/small/holder.svg'
                          : 'assets/svg/icon/small/holder_green.svg',
                      width: 14.0,
                      height: 14.0,
                    ),
                    const SizedBox(width: 4.0),
                    Text(
                      status,
                      style: bookShelfTitleStyle,
                    ),
                  ],
                ),
                SizedBox(
                  child: SvgPicture.asset(
                    'assets/svg/icon/right_arrow.svg',
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8.0),
            Container(
              width: double.infinity,
              height: 150.0,
              decoration: BoxDecoration(
                color: greyDDDDDD,
                borderRadius: BorderRadius.circular(8.0),
                boxShadow: [
                  BoxShadow(
                    color: commonBlackColor.withOpacity(0.3),
                    blurRadius: 2,
                    offset: const Offset(0, 1),
                    spreadRadius: 1,
                  )
                ],
              ),
              child: myBooksBookShelfData.isEmpty
                  ? Center(
                      child: Text(
                        '아직 등록된 책이 없어요.',
                        style: bookShelfTitleStyle.copyWith(
                          color: grey999999,
                        ),
                      ),
                    )
                  : Stack(
                      children: myBooksBookShelfData
                          .map(
                            (myBook) => Positioned(
                              left: myBooksBookShelfData.indexOf(myBook) *
                                  leftPosition,
                              child: Container(
                                width: 100.0,
                                height: 140.0,
                                decoration: ShapeDecoration(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(8.0),
                                  ),
                                  image: DecorationImage(
                                    image: NetworkImage(
                                      myBook.book!.thumbnailUrl!,
                                    ),
                                    fit: BoxFit.fill,
                                  ),
                                ),
                              ),
                            ),
                          )
                          .toList()
                          .reversed
                          .toList(),
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
