import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/profile/profile_edit/components/profile_body.dart';
import 'package:mybrary/ui/profile/profile_edit/components/profile_image.dart';
import 'package:mybrary/utils/logics/validate_utils.dart';

class ProfileEditScreen extends StatefulWidget {
  const ProfileEditScreen({super.key});

  @override
  State<ProfileEditScreen> createState() => _ProfileEditScreenState();
}

class _ProfileEditScreenState extends State<ProfileEditScreen> {
  late String _originProfileImageUrl;
  late TextEditingController _nicknameController;
  late TextEditingController _introductionController;

  final _profileRepository = ProfileRepository();
  late Future<ProfileResponseData> _profileData;

  Future<ProfileResponseData> getProfileEditData() async {
    final dio = Dio();
    final profileResponse = await dio.get(getApi(API.getUserProfile),
        options: Options(headers: {'User-Id': 'testId'}));

    final ProfileResponse result = commonResponseResult(
      profileResponse,
      () => ProfileResponse(
        status: profileResponse.data['status'],
        message: profileResponse.data['message'],
        data: ProfileResponseData.fromJson(
          profileResponse.data['data'],
        ),
      ),
    );

    _nicknameController = TextEditingController(text: result.data!.nickname!);
    _introductionController =
        TextEditingController(text: result.data!.introduction!);
    _originProfileImageUrl = result.data!.profileImageUrl!;

    return result.data!;
  }

  Future<void> _refreshData() async {
    setState(() {
      _profileData = getProfileEditData();
    });
  }

  File? _profileImage;
  late FormData profileImageData;
  final ImagePicker picker = ImagePicker();

  Future pickProfileImage(ImageSource imageSource) async {
    final image = await picker.pickImage(source: imageSource);

    if (image == null) return;

    setState(() {
      _profileImage = File(image.path);
    });
  }

  @override
  void initState() {
    super.initState();

    _refreshData();
  }

  @override
  void dispose() {
    _profileImage = null;
    _nicknameController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double bottomInset = MediaQuery.of(context).viewInsets.bottom;

    return Scaffold(
      appBar: profileEditAppBar(),
      backgroundColor: WHITE_COLOR,
      body: SingleChildScrollView(
        keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
        child: Padding(
          padding: EdgeInsets.symmetric(
            horizontal: 24.0,
            vertical: 16.0,
          ),
          child: FutureBuilder(
            future: _profileData,
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                final profileData = snapshot.data!;
                bool isDefaultImage =
                    profileData.profileImageUrl!.contains('default.jpg');

                return Column(
                  children: [
                    GestureDetector(
                      behavior: HitTestBehavior.opaque,
                      onTap: () {
                        if (!isDefaultImage) {
                          profileImageMenuBottomSheet();
                        } else {
                          pickProfileImage(ImageSource.gallery);
                        }
                      },
                      child: ProfileImage(
                        originProfileImageUrl: _originProfileImageUrl,
                        profileImage: _profileImage,
                      ),
                    ),
                    SizedBox(height: 24.0),
                    ProfileBody(
                      bottomInset: bottomInset,
                      nicknameController: _nicknameController,
                      introductionController: _introductionController,
                      saveProfileEditButton: _saveProfileEditButton,
                    ),
                  ],
                );
              }
              return const Center(
                child: CircularLoading(),
              );
            },
          ),
        ),
      ),
    );
  }

  void savedProfileSnackBar({
    required BuildContext context,
    required String snackBarText,
  }) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(snackBarText),
        duration: Duration(seconds: 1),
      ),
    );
  }

  AppBar profileEditAppBar() {
    return AppBar(
      elevation: 0,
      title: const Text('프로필 편집'),
      titleTextStyle: appBarTitleStyle,
      centerTitle: true,
      backgroundColor: WHITE_COLOR,
      foregroundColor: BLACK_COLOR,
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

  void onTapPhotoAlbum() {
    pickProfileImage(ImageSource.gallery);
    Navigator.pop(context);
  }

  void onTapDefaultImage() async {
    await _profileRepository.deleteProfileImage();

    _refreshData();

    if (!mounted) return;
    savedProfileSnackBar(
      context: context,
      snackBarText: '기본 이미지로 변경 되었습니다.',
    );

    Navigator.of(context).pop();
  }

  void validateAlert(BuildContext context) {
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (context) => AlertDialog(
        elevation: 0,
        content: Text(
          '닉네임을 다시 한 번 확인해주세요.',
          textAlign: TextAlign.center,
        ),
        contentTextStyle: TextStyle(
          color: BLACK_COLOR,
          fontSize: 15.0,
          fontWeight: FontWeight.w400,
        ),
        contentPadding: EdgeInsets.only(top: 24.0, bottom: 12.0),
        actions: [
          Center(
            child: confirmButton(),
          ),
        ],
      ),
    );
  }

  void profileImageMenuBottomSheet() {
    showModalBottomSheet(
      shape: bottomSheetStyle,
      backgroundColor: Colors.white,
      context: context,
      builder: (_) {
        return SizedBox(
          height: 180,
          child: Padding(
            padding: const EdgeInsets.symmetric(
              horizontal: 28.0,
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                TextButton(
                  style: TextButton.styleFrom(
                    backgroundColor: Colors.transparent,
                    foregroundColor: Colors.transparent,
                    surfaceTintColor: Colors.transparent,
                    splashFactory: NoSplash.splashFactory,
                  ),
                  onPressed: onTapPhotoAlbum,
                  child: Text(
                    '📷  라이브러리에서 선택',
                    style: bottomSheetMenuTextStyle,
                  ),
                ),
                SizedBox(height: 12.0),
                TextButton(
                  style: TextButton.styleFrom(
                    backgroundColor: Colors.transparent,
                    foregroundColor: Colors.transparent,
                    surfaceTintColor: Colors.transparent,
                    splashFactory: NoSplash.splashFactory,
                  ),
                  onPressed: onTapDefaultImage,
                  child: Text(
                    '📚  기본 이미지로 변경',
                    style: bottomSheetMenuTextStyle,
                  ),
                ),
                SizedBox(height: 12.0),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget confirmButton() {
    return SizedBox(
      width: MediaQuery.of(context).size.width * 0.6,
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          elevation: 0,
          backgroundColor: PRIMARY_COLOR,
          foregroundColor: WHITE_COLOR,
        ),
        onPressed: () {
          Navigator.pop(context);
        },
        child: Text('확인'),
      ),
    );
  }

  void _saveProfileEditButton() async {
    if (_nicknameController.text == '' ||
        checkAuthValidator(
            _nicknameController.text, LoginRegExp.nicknameRegExp, 2, 20)) {
      return validateAlert(context);
    } else {
      await _profileRepository.editProfileData(
        newNickname: _nicknameController.text,
        introduction: _introductionController.text,
      );

      if (_profileImage != null) {
        profileImageData = FormData.fromMap(
          {
            'profileImage': await MultipartFile.fromFile(_profileImage!.path),
          },
        );

        await _profileRepository.editProfileImage(
          newProfileImage: profileImageData,
        );
      }

      _refreshData();

      if (!mounted) return;
      savedProfileSnackBar(
        context: context,
        snackBarText: '변경 사항이 저장 되었습니다.',
      );

      Navigator.pop(context);
    }
  }
}
