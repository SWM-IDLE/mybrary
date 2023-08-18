import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class ProfileHeader extends StatelessWidget {
  final String? nickname;
  final String? profileImageUrl;
  final String followerCount;
  final String followingCount;
  final VoidCallback navigateToFollowScreen;
  final VoidCallback navigateToFollowingScreen;

  const ProfileHeader({
    required this.nickname,
    required this.profileImageUrl,
    required this.followerCount,
    required this.followingCount,
    required this.navigateToFollowScreen,
    required this.navigateToFollowingScreen,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: const BoxDecoration(
        color: greyF1F2F5,
      ),
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              width: 82.0,
              height: 82.0,
              decoration: ShapeDecoration(
                shape: RoundedRectangleBorder(
                  side: const BorderSide(
                    width: 2,
                    color: bookBorderColor,
                  ),
                  borderRadius: BorderRadius.circular(50),
                ),
                image: DecorationImage(
                  image: NetworkImage(
                    '$profileImageUrl',
                  ),
                  fit: BoxFit.cover,
                ),
              ),
            ),
            const SizedBox(height: 10.0),
            Text(
              nickname!,
              style: commonSubTitleStyle.copyWith(
                fontSize: 18.0,
              ),
            ),
            const SizedBox(height: 2.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                InkWell(
                  onTap: () {
                    navigateToFollowScreen();
                  },
                  child: Row(
                    children: [
                      const Text('팔로워', style: followTextStyle),
                      const SizedBox(width: 6.0),
                      Text(followerCount, style: followTextStyle),
                    ],
                  ),
                ),
                const SizedBox(width: 6.0),
                const Text(
                  '·',
                  style: followTextStyle,
                ),
                const SizedBox(width: 6.0),
                InkWell(
                  onTap: () {
                    navigateToFollowingScreen();
                  },
                  child: Row(
                    children: [
                      const Text('팔로잉', style: followTextStyle),
                      const SizedBox(width: 6.0),
                      Text(followingCount, style: followTextStyle),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8.0),
            const Text(
              '#초보 리뷰어',
              style: commonSubThinStyle,
            )
          ],
        ),
      ),
    );
  }
}
