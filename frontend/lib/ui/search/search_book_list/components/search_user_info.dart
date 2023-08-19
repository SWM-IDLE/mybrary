import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class SearchUserInfo extends StatelessWidget {
  final String nickname;
  final String profileImageUrl;

  const SearchUserInfo({
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
          backgroundColor: greyACACAC,
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
