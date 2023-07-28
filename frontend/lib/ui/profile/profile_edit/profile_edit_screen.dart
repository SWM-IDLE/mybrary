import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/ui/profile/profile_edit/components/profile_edit_form.dart';

class ProfileEditScreen extends StatefulWidget {
  const ProfileEditScreen({super.key});

  @override
  State<ProfileEditScreen> createState() => _ProfileEditScreenState();
}

class _ProfileEditScreenState extends State<ProfileEditScreen> {
  File? _profileImage;
  final picker = ImagePicker();

  Future getProfileImage(ImageSource imageSource) async {
    final image = await picker.pickImage(source: imageSource);

    if (image == null) return;

    setState(() {
      _profileImage = File(image.path);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: profileEditAppBar(),
      backgroundColor: WHITE_COLOR,
      resizeToAvoidBottomInset: false,
      body: Padding(
        padding: const EdgeInsets.symmetric(
          horizontal: 24.0,
          vertical: 16.0,
        ),
        child: Column(
          children: [
            GestureDetector(
              onTap: () {
                showModalBottomSheet(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.only(
                      topLeft: Radius.circular(20.0),
                      topRight: Radius.circular(20.0),
                    ),
                  ),
                  backgroundColor: Colors.white,
                  context: context,
                  builder: (_) {
                    return profileImageMenu();
                  },
                );
              },
              child: Align(
                alignment: Alignment.centerRight,
                child: SvgPicture.asset(
                  'assets/svg/icon/album.svg',
                  width: 24.0,
                ),
              ),
            ),
            Container(
              width: 96.0,
              height: 96.0,
              decoration: ShapeDecoration(
                shape: RoundedRectangleBorder(
                  side: BorderSide(width: 1, color: BOOK_BORDER_COLOR),
                  borderRadius: BorderRadius.circular(50),
                ),
                image: DecorationImage(
                  image: _profileImage == null
                      ? Image.asset('assets/img/icon/profile_default.png').image
                      : Image.file(File(_profileImage!.path)).image,
                  fit: BoxFit.cover,
                ),
              ),
            ),
            SizedBox(height: 24.0),
            ProfileEditForm(),
            SizedBox(height: 24.0),
            ElevatedButton(
              onPressed: () {},
              child: Text('저장'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size(double.infinity, 52.0),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                textStyle: TextStyle(
                  fontSize: 16.0,
                  fontWeight: FontWeight.w700,
                ),
                padding: EdgeInsets.symmetric(vertical: 14.0),
                backgroundColor: PRIMARY_COLOR,
              ),
            ),
          ],
        ),
      ),
    );
  }

  AppBar profileEditAppBar() {
    return AppBar(
      elevation: 0,
      title: Text('프로필 편집'),
      titleTextStyle: TextStyle(
        color: BLACK_COLOR,
        fontSize: 17.0,
        fontWeight: FontWeight.w700,
      ),
      centerTitle: true,
      backgroundColor: WHITE_COLOR,
      foregroundColor: BLACK_COLOR,
    );
  }

  Widget profileImageMenu() {
    return Container(
      height: 210,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 28.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GestureDetector(
              behavior: HitTestBehavior.opaque,
              onTap: () {
                Navigator.pop(context);
                getProfileImage(ImageSource.gallery);
              },
              child: profileImageMenuTab(
                Icons.photo_outlined,
                '라이브러리에서 선택',
              ),
            ),
            SizedBox(height: 24.0),
            GestureDetector(
              behavior: HitTestBehavior.opaque,
              onTap: () {
                Navigator.pop(context);
                getProfileImage(ImageSource.camera);
              },
              child: profileImageMenuTab(
                Icons.camera_alt_outlined,
                '사진 찍기',
              ),
            ),
            SizedBox(height: 24.0),
            GestureDetector(
              behavior: HitTestBehavior.opaque,
              onTap: () {
                Navigator.pop(context);
                setState(() {
                  _profileImage = null;
                });
              },
              child: profileImageMenuTab(
                Icons.delete_outline_rounded,
                '현재 사진 삭제',
                color: commonRedColor,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget profileImageMenuTab(IconData icon, String tabText, {Color? color}) {
    final menuTextStyle = TextStyle(
      height: 1.0,
      color: color,
      fontSize: 16.0,
      fontWeight: FontWeight.w400,
    );

    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Icon(
          icon,
          size: 28.0,
          color: color,
        ),
        SizedBox(width: 12.0),
        Text(
          tabText,
          style: menuTextStyle,
        ),
      ],
    );
  }
}
