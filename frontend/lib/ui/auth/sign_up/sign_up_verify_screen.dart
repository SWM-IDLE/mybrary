import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/ui/auth/components/logo.dart';
import 'package:mybrary/ui/auth/components/sign_in_input.dart';
import 'package:mybrary/ui/auth/components/sing_in_button.dart';

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
              Logo(
                logoText: '회원가입',
              ),
              Expanded(
                child: _SignUpVerifyForm(
                  emailCode: emailCode ?? '',
                  onSignUpSaved: (String? val) {
                    emailCode = val;
                  },
                  isVerifyEnabled: false,
                ),
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
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          _VerifyForm(
            emailCode: emailCode,
            onSignUpSaved: onSignUpSaved,
          ),
          Padding(
            padding: const EdgeInsets.only(bottom: 16.0),
            child: SingInButton(
              onPressed: () {},
              isEnabled: isVerifyEnabled,
              isOAuth: false,
              btnText: '가입하기',
              btnBackgroundColor: loginPrimaryColor,
              textColor: commonBlackColor,
            ),
          ),
        ],
      ),
    );
  }
}

class _VerifyForm extends StatelessWidget {
  final String emailCode;
  final FormFieldSetter<String> onSignUpSaved;

  const _VerifyForm({
    required this.emailCode,
    required this.onSignUpSaved,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final email = ModalRoute.of(context)!.settings.arguments;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Text('이메일'),
        Container(
          padding: EdgeInsets.symmetric(vertical: 16.0),
          decoration: BoxDecoration(
            border: Border(
              bottom: BorderSide(
                color: commonBlackColor,
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
        SignInInput(
          initialValue: emailCode,
          onSaved: onSignUpSaved,
          hintText: '이메일로 전송된 인증코드를 입력해주세요.',
          validator: (String? val) {
            // 인증코드는 숫자로만? 영문/숫자 혼합?
            // 추후 검증 로직이 추가될 예정입니다.
            return null;
          },
        ),
      ],
    );
  }
}
