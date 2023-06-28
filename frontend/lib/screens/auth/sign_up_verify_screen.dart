import 'package:flutter/material.dart';
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
  String? emailCode;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: SafeArea(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            children: [
              SizedBox(
                height: 50.0,
              ),
              Logo(
                logoText: '회원가입',
              ),
              SizedBox(
                height: 140.0,
              ),
              _SignUpVerifyForm(
                emailCode: emailCode ?? '',
                onSignUpSaved: (String? val) {
                  emailCode = val;
                },
                isVerifyEnabled: false,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _SignUpVerifyForm extends StatelessWidget {
  final String emailCode;
  final FormFieldSetter<String> onSignUpSaved;
  final bool isVerifyEnabled;

  const _SignUpVerifyForm({
    required this.emailCode,
    required this.onSignUpSaved,
    required this.isVerifyEnabled,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final email = ModalRoute.of(context)!.settings.arguments;
    return Form(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text('이메일'),
          Container(
            padding: EdgeInsets.symmetric(vertical: 16.0),
            decoration: BoxDecoration(
              border: Border(
                bottom: BorderSide(
                  color: BLACK_COLOR,
                  width: 1.0,
                ),
              ),
            ),
            child: Text(
              email.toString(),
            ),
          ),
          SizedBox(
            height: 20.0,
          ),
          Text('인증코드'),
          LoginInput(
            initialValue: emailCode,
            onSaved: onSignUpSaved,
            hintText: '이메일로 전송된 인증코드를 입력해주세요.',
            validator: (String? val) {
              // 인증코드는 숫자로만? 영문/숫자 혼합?
              // 추후 검증 로직이 추가될 예정입니다.
              return null;
            },
          ),
          SizedBox(
            height: 30.0,
          ),
          LoginButton(
            onPressed: () {},
            isEnabled: isVerifyEnabled,
            isOAuth: false,
            btnText: '가입하기',
            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
            textColor: BLACK_COLOR,
          ),
        ],
      ),
    );
  }
}
