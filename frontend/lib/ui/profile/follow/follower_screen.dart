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
          future: Future.wait([_followerResponseData, _followingResponseData])
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

              isFollowing(String loginId) {
                return followings
                    .any((following) => following.loginId == loginId);
              }

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
                                        followers[index].profileImageUrl!,
                                      ),
                                    ),
                                    SizedBox(
                                      width: 16.0,
                                    ),
                                    Text(
                                      followers[index].nickname!,
                                      style: followNicknameStyle,
                                    ),
                                  ],
                                ),
                                ElevatedButton(
                                  onPressed: () {
                                    setState(() {
                                      if (isFollowing(
                                        followers[index].loginId!,
                                      )) {
                                        _followRepository.deleteFollower(
                                          userId: 'testId',
                                          targetId: followers[index].loginId!,
                                        );
                                      } else {
                                        _followRepository.updateFollowing(
                                          userId: 'testId',
                                          targetId: followers[index].loginId!,
                                        );
                                      }
                                    });
                                  },
                                  style: ElevatedButton.styleFrom(
                                    elevation: 0,
                                    backgroundColor: GREY_02_COLOR,
                                    foregroundColor: BLACK_COLOR,
                                    padding: EdgeInsets.symmetric(
                                      horizontal: 16.0,
                                      vertical: 8.0,
                                    ),
                                    minimumSize: Size(60.0, 10.0),
                                  ),
                                  child: Text(
                                    isFollowing(followers[index].loginId!)
                                        ? '삭제'
                                        : '팔로우',
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
