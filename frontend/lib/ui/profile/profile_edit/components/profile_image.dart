import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';

class ProfileImage extends StatelessWidget {
  final File? profileImage;
  final String originProfileImageUrl;
  const ProfileImage({
    this.profileImage,
    required this.originProfileImageUrl,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          width: 96.0,
          height: 96.0,
          decoration: ShapeDecoration(
            shape: RoundedRectangleBorder(
              side: BorderSide(
                width: 1,
                color: BOOK_BORDER_COLOR,
              ),
              borderRadius: BorderRadius.circular(50),
            ),
            image: DecorationImage(
              image: profileImage == null
                  ? NetworkImage(
                      '$originProfileImageUrl?time=${DateTime.now().millisecondsSinceEpoch}',
                    )
                  : Image.file(File(profileImage!.path)).image,
              fit: BoxFit.cover,
            ),
          ),
        ),
        Positioned(
          right: 0,
          bottom: 0,
          child: SvgPicture.asset(
            'assets/svg/icon/profile_album.svg',
          ),
        ),
      ],
    );
  }
}
