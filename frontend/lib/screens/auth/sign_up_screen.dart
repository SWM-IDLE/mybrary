import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';

class SignUpScreen extends StatefulWidget {
  const SignUpScreen({Key? key}) : super(key: key);

  @override
  State<SignUpScreen> createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {
  String? signUpId;
  String? signUpEmail;
  String? signUpPassword;
  String? signUpPasswordCheck;
  String? signUpNickname;

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
                height: 50.0,
              ),
              _SignUpForm(
                signUpIdInitialValue: signUpId ?? '',
                signUpEmailInitialValue: signUpEmail ?? '',
                signUpPasswordInitialValue: signUpPassword ?? '',
                signUpPasswordCheckInitialValue: signUpPasswordCheck ?? '',
                signUpNicknameInitialValue: signUpNickname ?? '',
                onSignUpIdSaved: (String? val) {
                  signUpId = val;
                },
                onSignUpEmailSaved: (String? val) {
                  signUpEmail = val;
                },
                onSignUpPasswordSaved: (String? val) {
                  signUpPassword = val;
                },
                onSignUpPasswordConfirmSaved: (String? val) {
                  signUpPasswordCheck = val;
                },
                onSignUpNicknameSaved: (String? val) {
                  signUpNickname = val;
                },
                isEnabledSignUp: signUpId != null &&
                    signUpEmail != null &&
                    signUpPassword != null &&
                    signUpPasswordCheck != null &&
                    signUpNickname != null,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _SignUpForm extends StatelessWidget {
  final String signUpIdInitialValue;
  final String signUpEmailInitialValue;
  final String signUpPasswordInitialValue;
  final String signUpPasswordCheckInitialValue;
  final String signUpNicknameInitialValue;
  final FormFieldSetter<String> onSignUpIdSaved;
  final FormFieldSetter<String> onSignUpEmailSaved;
  final FormFieldSetter<String> onSignUpPasswordSaved;
  final FormFieldSetter<String> onSignUpPasswordConfirmSaved;
  final FormFieldSetter<String> onSignUpNicknameSaved;
  final bool isEnabledSignUp;

  const _SignUpForm({
    required this.signUpIdInitialValue,
    required this.signUpEmailInitialValue,
    required this.signUpPasswordInitialValue,
    required this.signUpPasswordCheckInitialValue,
    required this.signUpNicknameInitialValue,
    required this.onSignUpEmailSaved,
    required this.onSignUpIdSaved,
    required this.onSignUpPasswordSaved,
    required this.onSignUpPasswordConfirmSaved,
    required this.onSignUpNicknameSaved,
    required this.isEnabledSignUp,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final GlobalKey<FormState> signUpKey = GlobalKey();

    return Form(
      key: signUpKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          LoginInput(
            initialValue: signUpIdInitialValue,
            onSaved: onSignUpIdSaved,
            hintText: '아이디',
            validator: (String? val) {
              return null;
            },
          ),
          SizedBox(
            height: 10.0,
          ),
          LoginInput(
            initialValue: signUpEmailInitialValue,
            onSaved: onSignUpEmailSaved,
            hintText: '이메일',
            validator: (String? val) {
              return null;
            },
          ),
          SizedBox(
            height: 10.0,
          ),
          LoginInput(
            initialValue: signUpPasswordInitialValue,
            onSaved: onSignUpPasswordSaved,
            hintText: '비밀번호',
            validator: (String? val) {
              return null;
            },
          ),
          SizedBox(
            height: 10.0,
          ),
          LoginInput(
            initialValue: signUpPasswordCheckInitialValue,
            onSaved: onSignUpPasswordConfirmSaved,
            hintText: '비밀번호 확인',
            validator: (String? val) {
              return null;
            },
          ),
          SizedBox(
            height: 10.0,
          ),
          LoginInput(
            initialValue: signUpNicknameInitialValue,
            onSaved: onSignUpNicknameSaved,
            hintText: '닉네임',
            validator: (String? val) {
              return null;
            },
          ),
          SizedBox(
            height: 30.0,
          ),
          LoginButton(
            onPressed: () {
              Navigator.of(context).pushNamed('/signup/verify');
            },
            isOAuth: false,
            isEnabled: isEnabledSignUp,
            btnText: '인증하기',
            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
            textColor: BLACK_COLOR,
          ),
        ],
      ),
    );
  }
}
