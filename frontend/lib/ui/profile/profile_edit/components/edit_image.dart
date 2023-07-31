import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';

class EditImage extends StatelessWidget {
  final String profileImageUrl;
  final String originProfileImageUrl;
  final GestureTapCallback onTapPhotoAlbum;
  final GestureTapCallback onTapDefaultImage;
  final Future pickProfileImage;

  const EditImage({
    required this.profileImageUrl,
    required this.originProfileImageUrl,
    required this.onTapPhotoAlbum,
    required this.onTapDefaultImage,
    required this.pickProfileImage,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        if (profileImageUrl.substring(profileImageUrl.length - 11) !=
            'default.jpg') {
          showModalBottomSheet(
            shape: bottomSheetStyle,
            backgroundColor: Colors.white,
            context: context,
            builder: (_) {
              return SizedBox(
                height: 160,
                child: Padding(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 28.0,
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      GestureDetector(
                        behavior: HitTestBehavior.opaque,
                        onTap: onTapPhotoAlbum,
                        child: profileImageMenuTab(
                          tabText: '📷  라이브러리에서 선택',
                        ),
                      ),
                      SizedBox(height: 36.0),
                      GestureDetector(
                        behavior: HitTestBehavior.opaque,
                        onTap: onTapDefaultImage,
                        child: profileImageMenuTab(
                          tabText: '📚  기본 이미지로 변경',
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        } else {
          pickProfileImage;
        }
      },
      child: Stack(
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
                image: NetworkImage(originProfileImageUrl),
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
      ),
    );
  }

  Widget profileImageMenuTab({
    required String tabText,
  }) {
    return Text(
      tabText,
      style: bottomSheetMenuTextStyle,
    );
  }
}
