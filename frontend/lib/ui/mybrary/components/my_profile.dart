import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class MyProfile extends StatelessWidget {
  const MyProfile({super.key});

  @override
  Widget build(BuildContext context) {
    final followTextStyle = TextStyle(
      fontSize: 14.0,
      fontWeight: FontWeight.w400,
    );

    return Padding(
      padding: const EdgeInsets.symmetric(
        horizontal: 16.0,
        vertical: 20.0,
      ),
      child: Row(
        children: [
          Container(
            width: 82.0,
            height: 82.0,
            decoration: ShapeDecoration(
              shape: RoundedRectangleBorder(
                side: BorderSide(width: 2, color: BOOK_BORDER_COLOR),
                borderRadius: BorderRadius.circular(50),
              ),
              image: DecorationImage(
                image: NetworkImage(
                  // 프로필 임시 이미지
                  'https://blog.kakaocdn.net/dn/SDhEI/btqZWOAubQF/cNfyvunWb9cKg7DlaPE9mK/img.jpg',
                ),
                fit: BoxFit.cover,
              ),
            ),
          ),
          SizedBox(width: 12.0),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                '박보영',
                style: TextStyle(
                  fontSize: 18.0,
                  fontWeight: FontWeight.w700,
                ),
              ),
              SizedBox(
                height: 4.0,
              ),
              Wrap(
                spacing: 5,
                children: [
                  Text('팔로워', style: followTextStyle),
                  Text('264', style: followTextStyle),
                  SizedBox(width: 6.0),
                  Text('팔로잉', style: followTextStyle),
                  Text('123', style: followTextStyle),
                ],
              ),
            ],
          ),
        ],
      ),
    );
  }
}
