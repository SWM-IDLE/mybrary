import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_box_component.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';

class SignUpVerifyScreen extends StatefulWidget {
  const SignUpVerifyScreen({Key? key}) : super(key: key);

  @override
  State<SignUpVerifyScreen> createState() => _SignUpVerifyScreenState();
}

class _SignUpVerifyScreenState extends State<SignUpVerifyScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
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
                  signWidget: _SignUpVerfiyForm(),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _SignUpVerfiyForm extends StatelessWidget {
  const _SignUpVerfiyForm({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Form(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          LoginInput(
            hintText: '이메일',
            backgroundColor: LOGIN_INPUT_COLOR,
          ),
          SizedBox(
            height: 10.0,
          ),
          LoginInput(
            hintText: '인증코드',
            backgroundColor: LOGIN_INPUT_COLOR,
          ),
          SizedBox(
            height: 30.0,
          ),
          LoginButton(
            onPressed: () {},
            btnText: '가입하기',
            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
            textColor: Colors.black,
          ),
        ],
      ),
    );
  }
}
