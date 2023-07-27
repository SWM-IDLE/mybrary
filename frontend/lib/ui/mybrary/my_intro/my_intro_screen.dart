import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';

class MyIntroScreen extends StatelessWidget {
  const MyIntroScreen({super.key});

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
        Text(
          '마이브러리는 도서의 가치를 발견하고,\n사람을 잇는 서비스입니다.',
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
              icon: SvgPicture.asset('assets/svg/icon/right_arrow.svg'),
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
