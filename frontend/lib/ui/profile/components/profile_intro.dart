import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/profile/my_badge/my_badge_screen.dart';
import 'package:mybrary/ui/profile/my_interests/my_interests_screen.dart';

class ProfileIntro extends StatelessWidget {
  final String introduction;
  final VoidCallback onTapWriteIntroduction;

  const ProfileIntro({
    required this.introduction,
    required this.onTapWriteIntroduction,
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
          GestureDetector(
            onTap: () {
              if (introduction == '') {
                onTapWriteIntroduction();
              }
            },
            child: Text(
              introduction == '' ? '한 줄 소개 작성하기' : introduction,
              style: profileEditContentStyle.copyWith(
                decoration:
                    introduction == '' ? TextDecoration.underline : null,
              ),
            ),
          ),
          const SizedBox(height: 42.0),
          GestureDetector(
            behavior: HitTestBehavior.opaque,
            onTap: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (_) => const MyInterestsScreen(),
                ),
              );
            },
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Text(
                  '마이 관심사',
                  style: commonSubTitleStyle,
                ),
                SizedBox(
                  child: SvgPicture.asset(
                    'assets/svg/icon/right_arrow.svg',
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 12.0),
          Text(
            '장르소설, 심리학, 여행',
            style: profileEditContentStyle,
          ),
          const SizedBox(height: 42.0),
          GestureDetector(
            behavior: HitTestBehavior.opaque,
            onTap: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (_) => const MyBadgeScreen(),
                ),
              );
            },
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const Text(
                  '마이 뱃지',
                  style: commonSubTitleStyle,
                ),
                SizedBox(
                  child: SvgPicture.asset(
                    'assets/svg/icon/right_arrow.svg',
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 12.0),
          SizedBox(
            width: MediaQuery.of(context).size.width * 0.9,
            height: 72.0,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: 4,
              itemBuilder: (context, index) {
                final iconPath = [
                  'assets/svg/badge/beginner_reviewer.svg',
                  'assets/svg/badge/my_member.svg',
                  'assets/svg/badge/read_through.svg',
                  'assets/svg/badge/influencer.svg',
                ];

                return Container(
                  width: 72.0,
                  height: 72.0,
                  margin: const EdgeInsets.only(right: 10.0),
                  decoration: ShapeDecoration(
                    shape: RoundedRectangleBorder(
                      side: const BorderSide(
                        width: 1,
                        color: BOOK_BORDER_COLOR,
                      ),
                      borderRadius: BorderRadius.circular(50),
                    ),
                  ),
                  child: SvgPicture.asset(
                    iconPath[index],
                    fit: BoxFit.cover,
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
