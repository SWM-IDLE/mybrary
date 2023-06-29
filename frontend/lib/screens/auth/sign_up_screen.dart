import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';
import 'package:mybrary/utilities/regexps.dart';

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
      print('에러가 있습니다.');
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
        LoginInput(
          initialValue: signUpIdInitialValue,
          onSaved: onSignUpIdSaved,
          hintText: '영문, 숫자 포함 6자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '아이디를 입력해 주세요.';
            }

            if (val.length < 6 ||
                val.length > 20 ||
                !(LoginRegExp.idRegExp.hasMatch(val))) {
              return '아이디는 영문/숫자 포함 6자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('이메일'),
        LoginInput(
          initialValue: signUpEmailInitialValue,
          onSaved: onSignUpEmailSaved,
          hintText: 'mybrary@example.com',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '이메일을 입력해 주세요.';
            }

            if (val.length < 7 ||
                val.length > 40 ||
                !(LoginRegExp.emailRegExp.hasMatch(val))) {
              return '이메일 형식으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('비밀번호*'),
        LoginInput(
          initialValue: signUpPasswordInitialValue,
          onSaved: onSignUpPasswordSaved,
          hintText: '영문, 숫자, 특수문자 포함 8자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '비밀번호를 입력해 주세요.';
            }

            if (val.length < 8 ||
                val.length > 16 ||
                !(LoginRegExp.passwordRegExp.hasMatch(val))) {
              return '비밀번호는 영문/숫자/특수문자 혼합 8자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('비밀번호 확인*'),
        LoginInput(
          initialValue: signUpPasswordCheckInitialValue,
          onSaved: onSignUpPasswordConfirmSaved,
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
        LoginInput(
          initialValue: signUpNicknameInitialValue,
          onSaved: onSignUpNicknameSaved,
          hintText: '특수문자 제외 6자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '닉네임을 입력해 주세요.';
            }

            if (val.length < 6 ||
                val.length > 20 ||
                !(LoginRegExp.nicknameRegExp.hasMatch(val))) {
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
          child: LoginButton(
            onPressed: onVerifyPressed,
            isOAuth: false,
            isEnabled: isEnabledSignUp,
            btnText: '인증번호 발송',
            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
            textColor: BLACK_COLOR,
          ),
        ),
      ],
    );
  }
}
