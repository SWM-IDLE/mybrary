import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/ui/auth/components/logo.dart';
import 'package:mybrary/ui/auth/components/sign_in_input.dart';
import 'package:mybrary/ui/auth/components/sing_in_button.dart';
import 'package:mybrary/utils/logics/validate_utils.dart';

class SignUpScreen extends StatefulWidget {
  const SignUpScreen({Key? key}) : super(key: key);

  @override
  State<SignUpScreen> createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {
  final GlobalKey<FormState> signUpKey = GlobalKey();

  String? signUpId;
  String? signUpEmail;
  String? signUpPassword;
  String? signUpPasswordCheck;
  String? signUpNickname;
  bool? _isValid;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: SafeArea(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 16.0),
            child: Column(
              children: [
                Logo(
                  logoText: '회원가입',
                ),
                Form(
                  key: signUpKey,
                  onChanged: () {
                    final isValid = signUpKey.currentState!.validate();
                    if (isValid != _isValid) {
                      setState(() {
                        _isValid = isValid;
                      });
                    }
                  },
                  child: _SignUpForm(
                    onVerifyPressed: onVerifyPressed,
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
                    isEnabledSignUp: _isValid ?? false,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void onVerifyPressed() async {
    // formKey는 생성을 했는데, Form 위젯과 결합을 안했을 때는 null
    if (signUpKey.currentState == null) {
      return;
    }

    if (signUpKey.currentState!.validate()) {
      signUpKey.currentState!.save();

      Navigator.of(context).pushNamed(
        '/signup/verify',
        arguments: signUpEmail,
      );
    } else {
      log('ERROR: 서버 에러가 발생했습니다.');
    }
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
  final VoidCallback onVerifyPressed;

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
    required this.onVerifyPressed,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Text('아이디*'),
        SignInInput(
          initialValue: signUpIdInitialValue,
          onSaved: onSignUpIdSaved,
          hintText: '영문, 숫자 포함 6자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '아이디를 입력해 주세요.';
            }

            // 아이디의 길이는 6~20자 입니다.
            if (checkAuthValidator(val, LoginRegExp.idRegExp, 6, 20)) {
              return '아이디는 영문/숫자 포함 6자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('이메일'),
        SignInInput(
          initialValue: signUpEmailInitialValue,
          onSaved: onSignUpEmailSaved,
          hintText: 'mybrary@example.com',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '이메일을 입력해 주세요.';
            }

            // 이메일의 길이는 10~40자 입니다.
            if (checkAuthValidator(val, LoginRegExp.emailRegExp, 10, 40)) {
              return '이메일 형식으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('비밀번호*'),
        SignInInput(
          initialValue: signUpPasswordInitialValue,
          onSaved: onSignUpPasswordSaved,
          obscureText: true,
          hintText: '영문, 숫자, 특수문자 포함 8자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '비밀번호를 입력해 주세요.';
            }

            if (checkAuthValidator(val, LoginRegExp.passwordRegExp, 8, 16)) {
              return '비밀번호는 영문/숫자/특수문자 혼합 8자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('비밀번호 확인*'),
        SignInInput(
          initialValue: signUpPasswordCheckInitialValue,
          onSaved: onSignUpPasswordConfirmSaved,
          obscureText: true,
          hintText: '비밀번호 확인',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '비밀번호를 한번 더 입력해 주세요.';
            }

            // 비밀번호 확인 로직은 추후 input controller 구현 이후에 추가할 예정입니다.

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('닉네임'),
        SignInInput(
          initialValue: signUpNicknameInitialValue,
          onSaved: onSignUpNicknameSaved,
          hintText: '특수문자 제외 6자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '닉네임을 입력해 주세요.';
            }

            // 닉네임의 길이는 2~20자 입니다.
            if (checkAuthValidator(val, LoginRegExp.nicknameRegExp, 2, 20)) {
              return '닉네임은 특수문자 제외 6자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 30.0,
        ),
        Padding(
          padding: const EdgeInsets.only(bottom: 16.0),
          child: SingInButton(
            onPressed: onVerifyPressed,
            isOAuth: false,
            isEnabled: isEnabledSignUp,
            btnText: '인증번호 발송',
            btnBackgroundColor: loginPrimaryColor,
            textColor: commonBlackColor,
          ),
        ),
      ],
    );
  }
}
