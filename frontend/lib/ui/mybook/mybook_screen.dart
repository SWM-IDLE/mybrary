import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';

class MyBookScreen extends StatefulWidget {
  const MyBookScreen({super.key});

  @override
  State<MyBookScreen> createState() => _MyBookScreenState();
}

class _MyBookScreenState extends State<MyBookScreen> {
  final _profileRepository = ProfileRepository();

  late Future<ProfileResponseData> _profileResponseData;

  @override
  void initState() {
    super.initState();

    _profileResponseData = _profileRepository.getProfileData();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: FutureBuilder(
          future: _profileResponseData,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              final profileData = snapshot.data!;

              return CustomScrollView(
                physics: const BouncingScrollPhysics(
                  parent: AlwaysScrollableScrollPhysics(),
                ),
                slivers: [
                  _myBookAppBar(profileData),
                ],
              );
            }
            return const CircularLoading();
          }),
    );
  }

  SliverAppBar _myBookAppBar(ProfileResponseData profileData) {
    return SliverAppBar(
      toolbarHeight: 80.0,
      backgroundColor: WHITE_COLOR,
      elevation: 0,
      pinned: true,
      title: Row(
        children: [
          CircleAvatar(
            radius: 20.0,
            backgroundColor: GREY_03_COLOR,
            backgroundImage: NetworkImage(
              profileData.profileImageUrl!,
            ),
          ),
          const SizedBox(
            width: 12.0,
          ),
          Flexible(
            child: Text.rich(
              maxLines: 2,
              TextSpan(
                text: profileData.nickname!,
                style: myBookAppBarTextStyle.copyWith(
                  fontWeight: FontWeight.w700,
                ),
                children: const [
                  TextSpan(
                    text: '의 마이브러리',
                    style: myBookAppBarTextStyle,
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
      titleTextStyle: appBarTitleStyle,
      centerTitle: false,
      foregroundColor: BLACK_COLOR,
    );
  }
}
