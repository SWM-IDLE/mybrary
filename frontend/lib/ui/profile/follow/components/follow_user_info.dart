import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';

class FollowUserInfo extends StatelessWidget {
  final String nickname;
  final String profileImageUrl;

  const FollowUserInfo({
    required this.nickname,
    required this.profileImageUrl,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        CircleAvatar(
          radius: 20.0,
          backgroundColor: GREY_03_COLOR,
          backgroundImage: NetworkImage(
            profileImageUrl,
          ),
        ),
        const SizedBox(
          width: 16.0,
        ),
        Text(
          nickname,
          style: followNicknameStyle,
        ),
      ],
    );
  }
}
