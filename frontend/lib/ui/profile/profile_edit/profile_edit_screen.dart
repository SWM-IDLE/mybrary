import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:http_parser/http_parser.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
import 'package:mybrary/ui/common/components/single_data_error.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/ui/profile/profile_edit/components/profile_edit_body.dart';
import 'package:mybrary/ui/profile/profile_edit/components/profile_edit_image.dart';
import 'package:mybrary/utils/logics/validate_utils.dart';

class ProfileEditScreen extends StatefulWidget {
  const ProfileEditScreen({super.key});

  @override
  State<ProfileEditScreen> createState() => _ProfileEditScreenState();
}

class _ProfileEditScreenState extends State<ProfileEditScreen> {
  late String _originProfileImageUrl;
  late TextEditingController _nicknameController = TextEditingController();
  late TextEditingController _introductionController = TextEditingController();

  final _profileRepository = ProfileRepository();
  late Future<ProfileResponseData> _profileResponseData;

  final _userId = UserState.userId;

  Future<void> _refreshProfileData() async {
    setState(() {
      _profileResponseData = _profileRepository.getProfileData(
        context: context,
        userId: _userId,
      );
    });
  }

  File? _selectedImageFile;
  late FormData _profileImageFormData;
  final ImagePicker profileImagePicker = ImagePicker();

  Future pickProfileImage(ImageSource imageSource) async {
    final image = await profileImagePicker.pickImage(
      source: imageSource,
    );

    if (image == null) return;

    setState(() {
      _selectedImageFile = File(image.path);
    });
  }

  @override
  void initState() {
    super.initState();

    _profileRepository
        .getProfileData(
      context: context,
      userId: _userId,
    )
        .then((data) {
      _nicknameController = TextEditingController(
        text: data.nickname!,
      );
      _introductionController = TextEditingController(
        text: data.introduction!,
      );
    });

    _refreshProfileData();
  }

  @override
  void dispose() {
    _selectedImageFile = null;
    _nicknameController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double bottomInset = MediaQuery.of(context).viewInsets.bottom;

    return SubPageLayout(
      appBarTitle: '프로필 편집',
      backgroundColor: commonWhiteColor,
      child: LayoutBuilder(builder: (context, constraint) {
        return SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
          child: ConstrainedBox(
            constraints: BoxConstraints(
              minHeight: constraint.maxHeight,
            ),
            child: Padding(
              padding:
                  const EdgeInsets.symmetric(horizontal: 24.0, vertical: 16.0),
              child: FutureBuilder(
                future: _profileResponseData,
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return const SingleDataError(
                      errorMessage: '프로필 데이터를 불러오는데 실패했습니다.',
                    );
                  }

                  if (snapshot.hasData) {
                    final profileData = snapshot.data!;

                    _originProfileImageUrl = profileData.profileImageUrl!;
                    bool isDefaultImage =
                        profileData.profileImageUrl!.contains('default.jpg');

                    return Column(
                      children: [
                        GestureDetector(
                          behavior: HitTestBehavior.opaque,
                          onTap: () {
                            if (!isDefaultImage) {
                              _profileImageMenuBottomSheet();
                            } else {
                              pickProfileImage(ImageSource.gallery);
                            }
                          },
                          child: ProfileEditImage(
                            originProfileImageUrl: _originProfileImageUrl,
                            profileImage: _selectedImageFile,
                          ),
                        ),
                        const SizedBox(height: 24.0),
                        ProfileEditBody(
                          bottomInset: bottomInset,
                          nicknameController: _nicknameController,
                          introductionController: _introductionController,
                          saveProfileEditButton: _saveUserProfile,
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
      }),
    );
  }

  void _showProfileSavedMessage({
    required BuildContext context,
    required String snackBarText,
  }) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(
          snackBarText,
          style: commonSnackBarMessageStyle,
        ),
        duration: const Duration(
          seconds: 1,
        ),
      ),
    );
  }

  void _onTapSelectToPhotoAlbum() {
    pickProfileImage(ImageSource.gallery);
    Navigator.pop(context);
  }

  void _onTapChangeToDefaultImage() async {
    await _profileRepository.deleteProfileImage(
      context: context,
      userId: _userId,
    );

    _refreshProfileData();

    if (!mounted) return;
    _showProfileSavedMessage(
      context: context,
      snackBarText: '기본 이미지로 변경 되었습니다.',
    );

    Navigator.of(context).pop();
  }

  void _showValidationFailedMessage(BuildContext context) {
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (context) => AlertDialog(
        elevation: 0,
        content: const Text(
          '닉네임을 다시 한 번 확인해주세요.',
          textAlign: TextAlign.center,
        ),
        contentTextStyle: commonDialogMessageStyle,
        contentPadding: const EdgeInsets.only(
          top: 24.0,
          bottom: 12.0,
        ),
        actions: [
          Center(
            child: confirmButton(),
          ),
        ],
      ),
    );
  }

  void _profileImageMenuBottomSheet() {
    showModalBottomSheet(
      shape: bottomSheetStyle,
      backgroundColor: Colors.white,
      context: context,
      builder: (_) {
        return SizedBox(
          height: 160,
          child: Padding(
            padding: const EdgeInsets.symmetric(
              vertical: 20.0,
              horizontal: 28.0,
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Expanded(
                  child: TextButton(
                    style: commonMenuButtonStyle.copyWith(
                      alignment: Alignment.centerLeft,
                    ),
                    onPressed: _onTapSelectToPhotoAlbum,
                    child: const Text(
                      '📷  라이브러리에서 선택',
                      style: bottomSheetMenuTextStyle,
                    ),
                  ),
                ),
                Expanded(
                  child: TextButton(
                    style: commonMenuButtonStyle.copyWith(
                      alignment: Alignment.centerLeft,
                    ),
                    onPressed: _onTapChangeToDefaultImage,
                    child: const Text(
                      '📚  기본 이미지로 변경',
                      style: bottomSheetMenuTextStyle,
                    ),
                  ),
                ),
                const SizedBox(height: 6.0),
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
          backgroundColor: primaryColor,
          foregroundColor: commonWhiteColor,
        ),
        onPressed: () {
          Navigator.pop(context);
        },
        child: const Text('확인'),
      ),
    );
  }

  void _saveUserProfile() async {
    if (_nicknameController.text == '' ||
        checkAuthValidator(
          _nicknameController.text,
          LoginRegExp.nicknameRegExp,
          2,
          20,
        )) {
      return _showValidationFailedMessage(context);
    } else {
      await _profileRepository.updateProfileData(
        context: context,
        userId: _userId,
        newNickname: _nicknameController.text,
        introduction: _introductionController.text,
      );

      if (_selectedImageFile != null) {
        _profileImageFormData = FormData.fromMap(
          {
            'profileImage': await MultipartFile.fromFile(
              _selectedImageFile!.path,
              contentType: MediaType(
                'image',
                'jpg',
              ),
            ),
          },
        );

        await _profileRepository.updateProfileImage(
          context: context,
          userId: _userId,
          newProfileImage: _profileImageFormData,
        );
      }

      if (!mounted) return;
      _showProfileSavedMessage(
        context: context,
        snackBarText: '변경 사항이 저장 되었습니다.',
      );

      Navigator.pop(context);
    }
  }
}
