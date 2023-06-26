import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_box_component.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';

class SignUpScreen extends StatelessWidget {
  const SignUpScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0),
          child: IntrinsicHeight(
            child: Column(
              children: [
                SizedBox(
                  height: 50.0,
                ),
                Logo(
                  logoText: '회원가입',
                ),
                SizedBox(
                  height: 50.0,
                ),
                LoginBox(
                  signWidget: _SignUpForm(),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _SignUpForm extends StatelessWidget {
  const _SignUpForm({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        LoginInput(
          hintText: '아이디',
          backgroundColor: LOGIN_INPUT_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginInput(
          hintText: '이메일',
          backgroundColor: LOGIN_INPUT_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginInput(
          hintText: '비밀번호',
          backgroundColor: LOGIN_INPUT_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginInput(
          hintText: '비밀번호 확인',
          backgroundColor: LOGIN_INPUT_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginInput(
          hintText: '닉네임',
          backgroundColor: LOGIN_INPUT_COLOR,
        ),
        SizedBox(
          height: 30.0,
        ),
        LoginButton(
          btnText: '인증하기',
          btnBackgroundColor: LOGIN_PRIMARY_COLOR,
          textColor: Colors.black,
        ),
      ],
    );
  }
}
