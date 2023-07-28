import 'package:flutter/material.dart';
import 'package:mybrary/res/colors/color.dart';

class ProfileEditForm extends StatelessWidget {
  const ProfileEditForm({super.key});

  @override
  Widget build(BuildContext context) {
    final inputHintStyle = TextStyle(
      color: inputHintColor,
      fontSize: 14.0,
      fontWeight: FontWeight.w400,
    );

    final nameInputBorderStyle = UnderlineInputBorder(
      borderSide: BorderSide(
        color: inputBorderColor,
        width: 1,
      ),
    );

    final introInputBorderStyle = OutlineInputBorder(
      borderSide: BorderSide(
        color: inputBorderColor,
        width: 1,
      ),
    );

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            Text('이름'),
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
          decoration: InputDecoration(
            hintText: '이름을 입력해주세요.',
            hintStyle: inputHintStyle,
            border: nameInputBorderStyle,
            enabledBorder: nameInputBorderStyle,
            focusedBorder: nameInputBorderStyle,
          ),
        ),
        SizedBox(height: 24.0),
        Text('한 줄 소개'),
        SizedBox(height: 12.0),
        TextFormField(
          maxLines: 3,
          maxLength: 100,
          decoration: InputDecoration(
            contentPadding: EdgeInsets.all(16.0),
            hintText: '나를 한 줄로 표현해보세요.',
            hintStyle: inputHintStyle,
            border: introInputBorderStyle,
            enabledBorder: introInputBorderStyle,
            focusedBorder: introInputBorderStyle,
          ),
        ),
      ],
    );
  }
}
