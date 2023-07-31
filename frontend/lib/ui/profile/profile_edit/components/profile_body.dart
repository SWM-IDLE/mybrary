import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/constants/style.dart';

class ProfileBody extends StatelessWidget {
  final double bottomInset;
  final TextEditingController nicknameController;
  final TextEditingController introductionController;
  final VoidCallback saveProfileEditButton;

  const ProfileBody({
    required this.bottomInset,
    required this.nicknameController,
    required this.introductionController,
    required this.saveProfileEditButton,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
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
        const SizedBox(height: 8.0),
        TextFormField(
          controller: nicknameController,
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
        const SizedBox(height: 18.0),
        const Text(
          '한 줄 소개',
          style: profileEditTitleStyle,
        ),
        const SizedBox(height: 8.0),
        TextFormField(
          maxLines: 3,
          maxLength: 100,
          controller: introductionController,
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
        const SizedBox(height: 18.0),
        ElevatedButton(
          onPressed: saveProfileEditButton,
          style: ElevatedButton.styleFrom(
            minimumSize: const Size(
              double.infinity,
              52.0,
            ),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10.0),
            ),
            textStyle: commonButtonTextStyle,
            padding: const EdgeInsets.symmetric(
              vertical: 14.0,
            ),
            backgroundColor: PRIMARY_COLOR,
            disabledForegroundColor: WHITE_COLOR,
          ),
          child: const Text('저장'),
        ),
      ],
    );
  }
}
