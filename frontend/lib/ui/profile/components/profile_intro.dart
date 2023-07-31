import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/profile/profile_edit/profile_edit_screen.dart';
import 'package:mybrary/utils/animation/route_animation.dart';

class ProfileIntro extends StatelessWidget {
  final String introduction;

  const ProfileIntro({
    required this.introduction,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          '한 줄 소개',
          style: TextStyle(
            fontSize: 16.0,
            fontWeight: FontWeight.w700,
          ),
        ),
        SizedBox(height: 16.0),
        GestureDetector(
          onTap: () {
            if (introduction == '') {
              RouteAnimation routeAnimation =
                  RouteAnimation(ProfileEditScreen());
              Navigator.push(context, routeAnimation.slideRightToLeft());
            }
          },
          child: Text(
            introduction == '' ? '한 줄 소개 작성하기' : introduction,
            style: TextStyle(
              decoration: introduction == '' ? TextDecoration.underline : null,
              color: GREY_05_COLOR,
              fontSize: 13.0,
              fontWeight: FontWeight.w400,
            ),
          ),
        ),
        SizedBox(height: 28.0),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              '마이 관심사',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w700,
              ),
            ),
            IconButton(
              onPressed: () {},
              icon: SvgPicture.asset('assets/svg/icon/right_arrow.svg'),
            ),
          ],
        ),
        SizedBox(height: 4.0),
        Text(
          '장르소설, 심리학, 여행',
          style: TextStyle(
            color: GREY_05_COLOR,
            fontSize: 13.0,
            fontWeight: FontWeight.w400,
          ),
        ),
        SizedBox(height: 28.0),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              '마이 뱃지',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w700,
              ),
            ),
            IconButton(
              onPressed: () {},
              icon: SvgPicture.asset('assets/svg/icon/right_arrow.svg',
                  theme: SvgTheme(
                    currentColor: PRIMARY_COLOR,
                  )),
            ),
          ],
        ),
        SizedBox(height: 4.0),
        Container(
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
                margin: EdgeInsets.only(right: 10.0),
                decoration: ShapeDecoration(
                  shape: RoundedRectangleBorder(
                    side: BorderSide(width: 1, color: BOOK_BORDER_COLOR),
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
    );
  }
}
