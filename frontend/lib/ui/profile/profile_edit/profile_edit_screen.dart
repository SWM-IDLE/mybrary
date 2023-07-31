import 'dart:io';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mybrary/data/model/profile/profile_response.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/data/repository/profile_repository.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/components/circular_loading.dart';
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

  Future<ProfileResponseData> getProfileData() async {
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
      _profileData = getProfileData();
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
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
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
                        } else {
                          pickProfileImage(ImageSource.gallery);
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
                                image: _profileImage == null
                                    ? NetworkImage(
                                        '$_originProfileImageUrl?time=${DateTime.now().millisecondsSinceEpoch}')
                                    : Image.file(File(_profileImage!.path))
                                        .image,
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
                    ),
                    SizedBox(height: 24.0),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Row(
                          children: [
                            Text(
                              '닉네임',
                              style: profileEditTitleStyle,
                            ),
                            Text(
                              '*',
                              style: TextStyle(
                                color: commonOrangeColor,
                              ),
                            ),
                          ],
                        ),
                        SizedBox(height: 8.0),
                        TextFormField(
                          controller: _nicknameController,
                          maxLength: 20,
                          scrollPadding: EdgeInsets.only(
                            bottom: bottomInset,
                          ),
                          onEditingComplete: () {
                            FocusScope.of(context).unfocus();
                          },
                          decoration: const InputDecoration(
                            contentPadding: EdgeInsets.all(16.0),
                            hintText: '이름을 입력해주세요.',
                            hintStyle: inputHintStyle,
                            counter: SizedBox.shrink(),
                            border: introInputBorderStyle,
                            enabledBorder: introInputBorderStyle,
                            focusedBorder: introInputBorderStyle,
                            errorStyle: TextStyle(
                              decoration: TextDecoration.none,
                            ),
                          ),
                        ),
                        const Padding(
                          padding: EdgeInsets.symmetric(horizontal: 2.0),
                          child: Text(
                            '한글, 영문, 숫자 (2-20자)',
                            style: TextStyle(
                              color: GREY_05_COLOR,
                              fontSize: 13.0,
                              fontWeight: FontWeight.w400,
                            ),
                          ),
                        ),
                        SizedBox(height: 18.0),
                        Text(
                          '한 줄 소개',
                          style: profileEditTitleStyle,
                        ),
                        SizedBox(height: 8.0),
                        TextFormField(
                          maxLines: 3,
                          maxLength: 100,
                          controller: _introductionController,
                          scrollPadding: EdgeInsets.only(
                            bottom: bottomInset,
                          ),
                          onEditingComplete: () {
                            FocusScope.of(context).unfocus();
                          },
                          decoration: const InputDecoration(
                            contentPadding: EdgeInsets.all(16.0),
                            hintText: '나를 한 줄로 표현해보세요.',
                            hintStyle: inputHintStyle,
                            border: introInputBorderStyle,
                            enabledBorder: introInputBorderStyle,
                            focusedBorder: introInputBorderStyle,
                          ),
                        ),
                        SizedBox(height: 18.0),
                        ElevatedButton(
                          onPressed: () async {
                            if (_nicknameController.text == '' ||
                                checkAuthValidator(_nicknameController.text,
                                    LoginRegExp.nicknameRegExp, 2, 20)) {
                              return showDialog(
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
                                  contentPadding:
                                      EdgeInsets.only(top: 24.0, bottom: 12.0),
                                  actions: [
                                    Center(
                                      child: SizedBox(
                                        width:
                                            MediaQuery.of(context).size.width *
                                                0.6,
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
                                      ),
                                    ),
                                  ],
                                ),
                              );
                            } else {
                              await _profileRepository.editProfileData(
                                newNickname: _nicknameController.text,
                                introduction: _introductionController.text,
                              );

                              if (_profileImage != null) {
                                profileImageData = FormData.fromMap(
                                  {
                                    'profileImage':
                                        await MultipartFile.fromFile(
                                            _profileImage!.path),
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
                          },
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
                            disabledForegroundColor: WHITE_COLOR,
                          ),
                          child: const Text('저장'),
                        ),
                      ],
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
}
