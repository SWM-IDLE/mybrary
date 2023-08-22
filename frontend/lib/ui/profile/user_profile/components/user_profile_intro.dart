import 'package:flutter/material.dart';
import 'package:mybrary/data/model/profile/my_interests_response.dart';
import 'package:mybrary/res/constants/style.dart';

class UserProfileIntro extends StatelessWidget {
  final String introduction;
  final List<UserInterests> userInterests;

  const UserProfileIntro({
    required this.introduction,
    required this.userInterests,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(
        horizontal: 20.0,
        vertical: 24.0,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '한 줄 소개',
            style: commonSubTitleStyle,
          ),
          const SizedBox(height: 12.0),
          Text(
            introduction == '' ? '한 줄 소개가 없어요.' : introduction,
            style: commonEditContentStyle,
          ),
          const SizedBox(height: 42.0),
          const Text(
            '마이 관심사',
            style: commonSubTitleStyle,
          ),
          const SizedBox(height: 12.0),
          Text(
            userInterests.isEmpty
                ? '마이 관심사가 없어요.'
                : userInterests.map((interest) => interest.name).join(', '),
            style: commonEditContentStyle,
          ),
          const SizedBox(height: 12.0),
        ],
      ),
    );
  }
}
