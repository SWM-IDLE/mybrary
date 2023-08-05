import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/follower_response.dart';
import 'package:mybrary/data/model/profile/following_response.dart';
import 'package:mybrary/data/model/profile/profile_common_response.dart';
import 'package:mybrary/data/repository/follow_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';

class FollowerScreen extends StatefulWidget {
  final String nickname;

  const FollowerScreen({
    required this.nickname,
    super.key,
  });

  @override
  State<FollowerScreen> createState() => _FollowerScreenState();
}

class _FollowerScreenState extends State<FollowerScreen>
    with TickerProviderStateMixin {
  late List<String> _followTabs = ['팔로워', '팔로잉'];

  Set<String> followerUsers = {};

  bool isFollower(String loginId) {
    return !followerUsers.contains(loginId);
  }

  void toggleFollower(String loginId) {
    setState(() {
      if (isFollower(loginId)) {
        followerUsers.add(loginId);
      } else {
        followerUsers.remove(loginId);
      }
    });
  }

  void deleteFollower(String loginId) {
    setState(() {
      followerUsers.add(loginId);
      print(followerUsers);
    });
  }

  late final TabController _tabController = TabController(
    length: _followTabs.length,
    vsync: this,
  );

  final ScrollController _scrollController = ScrollController();
  final double _height = Size.fromHeight(
        const SliverAppBar().toolbarHeight,
      ).height *
      1.9;

  final _followRepository = FollowRepository();

  late Future<FollowerResponseData> _followerResponseData;
  late Future<FollowingResponseData> _followingResponseData;

  @override
  void initState() {
    super.initState();

    _followerResponseData = _followRepository.getFollower(
      userId: 'testId',
    );
    _followingResponseData = _followRepository.getFollowings(
      userId: 'testId',
    );
  }

  @override
  void dispose() {
    _tabController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: false,
      bottom: false,
      child: SubPageLayout(
        child: FutureBuilder<FollowCommonData>(
          future: Future.wait(futureFollowData())
              .then((followData) => FollowCommonData(
                    followerData: followData[0] as FollowerResponseData,
                    followingData: followData[1] as FollowingResponseData,
                  )),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              FollowCommonData data = snapshot.data!;
              final followers = data.followerData.followers!;
              final followings = data.followingData.followings!;

              _followTabs = <String>[
                '팔로워 ${followers.length}',
                '팔로잉 ${followings.length}',
              ];

              return NestedScrollView(
                controller: _scrollController,
                headerSliverBuilder:
                    (BuildContext context, bool innerBoxIsScrolled) {
                  return followPageSliverBuilder(
                    context,
                    innerBoxIsScrolled,
                    _followTabs,
                    _tabController,
                  );
                },
                body: TabBarView(
                  controller: _tabController,
                  children: <Widget>[
                    Padding(
                      padding: EdgeInsets.only(top: _height),
                      child: ListView.builder(
                        itemCount: followers.length,
                        itemBuilder: (context, index) {
                          Followers follower = followers[index];

                          return Padding(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 16.0,
                              vertical: 8.0,
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Row(
                                  children: [
                                    CircleAvatar(
                                      radius: 20.0,
                                      backgroundColor: GREY_03_COLOR,
                                      backgroundImage: NetworkImage(
                                        follower.profileImageUrl!,
                                      ),
                                    ),
                                    const SizedBox(
                                      width: 16.0,
                                    ),
                                    Text(
                                      follower.nickname!,
                                      style: followNicknameStyle,
                                    ),
                                  ],
                                ),
                                ElevatedButton(
                                  onPressed: () async {
                                    await _followRepository.deleteFollower(
                                      userId: 'testId',
                                      sourceId: follower.loginId!,
                                    );

                                    if (!mounted) return;
                                    _showDeletedFollowerMessage(context);

                                    Future.delayed(const Duration(seconds: 1),
                                        () {
                                      setState(() {
                                        followers.removeAt(index);
                                      });
                                      Navigator.of(context).pop();
                                    });
                                  },
                                  style: ElevatedButton.styleFrom(
                                    elevation: 0,
                                    backgroundColor: GREY_02_COLOR,
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 16.0,
                                      vertical: 8.0,
                                    ),
                                    minimumSize: const Size(60.0, 10.0),
                                    splashFactory: NoSplash.splashFactory,
                                  ),
                                  child: const Text(
                                    '삭제',
                                    style: followButtonTextStyle,
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
                    Padding(
                      padding: EdgeInsets.only(top: _height),
                      child: ListView.builder(
                        itemCount: followings.length,
                        itemBuilder: (context, index) {
                          return Padding(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 16.0,
                              vertical: 8.0,
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Row(
                                  children: [
                                    CircleAvatar(
                                      radius: 20.0,
                                      backgroundColor: GREY_03_COLOR,
                                      backgroundImage: NetworkImage(
                                        followings[index].profileImageUrl!,
                                      ),
                                    ),
                                    SizedBox(
                                      width: 16.0,
                                    ),
                                    Text(
                                      followings[index].nickname!,
                                      style: followNicknameStyle,
                                    ),
                                  ],
                                ),
                                ElevatedButton(
                                  onPressed: () {},
                                  style: ElevatedButton.styleFrom(
                                    elevation: 0,
                                    backgroundColor: GREY_02_COLOR,
                                    foregroundColor: BLACK_COLOR,
                                    padding: EdgeInsets.symmetric(
                                      horizontal: 16.0,
                                      vertical: 8.0,
                                    ),
                                    minimumSize: Size(60.0, 10.0),
                                    splashFactory: NoSplash.splashFactory,
                                  ),
                                  child: Text(
                                    '팔로잉',
                                    style: followButtonTextStyle,
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
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

  Future<dynamic> _showDeletedFollowerMessage(BuildContext context) {
    return showDialog(
      context: context,
      barrierColor: BLACK_COLOR.withOpacity(0.1),
      barrierDismissible: false,
      builder: (BuildContext context) {
        return Center(
          child: Container(
            width: 76.0,
            height: 38.0,
            decoration: BoxDecoration(
              color: GREY_06_COLOR.withOpacity(0.8),
              borderRadius: BorderRadius.circular(10.0),
            ),
            child: Center(
              child: Text(
                '삭제중',
                style: commonSubRegularStyle.copyWith(
                  color: WHITE_COLOR,
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  List<Future<Object>> futureFollowData() => [
        _followerResponseData,
        _followingResponseData,
      ];

  List<Widget> followPageSliverBuilder(
    BuildContext context,
    bool innerBoxIsScrolled,
    List<String> followerTabs,
    TabController tabController,
  ) {
    return <Widget>[
      SliverOverlapAbsorber(
        handle: NestedScrollView.sliverOverlapAbsorberHandleFor(context),
        sliver: SliverAppBar(
          elevation: 0,
          foregroundColor: BLACK_COLOR,
          backgroundColor: WHITE_COLOR,
          // expandedHeight: 120,
          flexibleSpace: FlexibleSpaceBar(
            background: Container(
              color: WHITE_COLOR,
            ),
          ),
          title: Text(widget.nickname),
          titleTextStyle: appBarTitleStyle.copyWith(
            fontSize: 16.0,
          ),
          centerTitle: true,
          pinned: true,
          expandedHeight: 104.01,
          forceElevated: innerBoxIsScrolled,
          bottom: TabBar(
            controller: tabController,
            indicatorColor: GREY_06_COLOR,
            labelColor: GREY_06_COLOR,
            labelStyle: commonButtonTextStyle,
            unselectedLabelColor: GREY_03_COLOR,
            unselectedLabelStyle: commonButtonTextStyle.copyWith(
              fontWeight: FontWeight.w400,
            ),
            tabs: followerTabs
                .map((String name) => Tab(
                      text: name,
                    ))
                .toList(),
          ),
        ),
      ),
    ];
  }
}
