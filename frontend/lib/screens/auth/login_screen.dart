import 'package:flutter/material.dart';
import 'package:mybrary/components/login/login_button_component.dart';
import 'package:mybrary/components/login/login_input_component.dart';
import 'package:mybrary/components/login/login_logo_component.dart';
import 'package:mybrary/constants/color.dart';
import 'package:mybrary/data/network/service/auth_apis.dart';
import 'package:mybrary/utilities/regexps.dart';

const LOGIN_TEST_ID = 'test123';
const LOGIN_TEST_PASSWORD = 'test123!@';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final GlobalKey<FormState> formKey = GlobalKey();

  String? loginId;
  String? loginPassword;
  bool? _isValid;

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
                logoText: '마이브러리',
              ),
              Expanded(
                child: Form(
                  key: formKey,
                  onChanged: () {
                    final isValid = formKey.currentState!.validate();
                    if (isValid != _isValid) {
                      setState(() {
                        _isValid = isValid;
                      });
                    }
                  },
                  child: Column(
                    children: [
                      _SelfLogin(
                        onSavePressed: onSavePressed,
                        loginIdInitialValue: loginId ?? '',
                        loginPasswordInitialValue: loginPassword ?? '',
                        onLoginIdSaved: (String? val) {
                          loginId = val;
                        },
                        onLoginPasswordSaved: (String? val) {
                          loginPassword = val;
                        },
                        isEnabledLogin: _isValid ?? false,
                      ),
                      SizedBox(
                        height: 20.0,
                      ),
                      _DividerLogin(),
                      SizedBox(
                        height: 20.0,
                      ),
                      _OAuthLogin(),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget forgotText(String text) {
    return Text(
      text,
      style: TextStyle(
        color: LOGIN_PRIMARY_COLOR,
        fontSize: 15.0,
        fontWeight: FontWeight.w600,
      ),
      textAlign: TextAlign.center,
    );
  }

  void onSavePressed() async {
    // formKey는 생성을 했는데, Form 위젯과 결합을 안했을 때는 null
    if (formKey.currentState == null) {
      return;
    }

    if (formKey.currentState!.validate()) {
      formKey.currentState!.save();
      if (loginId == LOGIN_TEST_ID && loginPassword == LOGIN_TEST_PASSWORD) {
        Navigator.of(context).pushNamed('/home');
      } else {
        print('아이디 또는 비밀번호가 틀렸습니다.');
      }
    } else {
      print('Log: 서버 에러');
    }
  }
}

class _SelfLogin extends StatelessWidget {
  final String loginIdInitialValue;
  final String loginPasswordInitialValue;
  final FormFieldSetter<String> onLoginIdSaved;
  final FormFieldSetter<String> onLoginPasswordSaved;
  final bool isEnabledLogin;

  final VoidCallback onSavePressed;
  const _SelfLogin({
    required this.onSavePressed,
    required this.loginIdInitialValue,
    required this.loginPasswordInitialValue,
    required this.onLoginIdSaved,
    required this.onLoginPasswordSaved,
    required this.isEnabledLogin,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Text('아이디'),
        LoginInput(
          initialValue: loginIdInitialValue,
          onSaved: onLoginIdSaved,
          hintText: '영문, 숫자 포함 6자 이상',
          validator: (String? val) {
            if (val == null || val.isEmpty) {
              return '아이디를 입력해 주세요.';
            }

            if (val.length < 6 ||
                val.length > 20 ||
                !(LoginRegExp.idRegExp.hasMatch(val))) {
              return '아이디는 영소문자/숫자 혼합 6자 이상으로 입력해 주세요.';
            }

            return null;
          },
        ),
        SizedBox(
          height: 20.0,
        ),
        Text('비밀번호'),
        LoginInput(
          initialValue: loginPasswordInitialValue,
          onSaved: onLoginPasswordSaved,
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
          height: 25.0,
        ),
        LoginButton(
          onPressed: onSavePressed,
          isOAuth: false,
          isEnabled: isEnabledLogin,
          btnText: '로그인',
          btnBackgroundColor: LOGIN_PRIMARY_COLOR,
          textColor: BLACK_COLOR,
        ),
        SizedBox(
          height: 25.0,
        ),
        GestureDetector(
          onTap: () {
            Navigator.of(context).pushNamed('/login/findpw');
          },
          child: _ForgotText(
            forgotText: '비밀번호를 잊으셨나요?',
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }
}

class _DividerLogin extends StatelessWidget {
  const _DividerLogin({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        _DividerLine(),
        _DividerLine(),
      ],
    );
  }
}

class _OAuthLogin extends StatelessWidget {
  const _OAuthLogin({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        LoginButton(
          onPressed: () async {
            signInOAuth(oAuthType: OAuthType.google);
          },
          isOAuth: true,
          isEnabled: true,
          btnIcon: Image.asset(
            'assets/images/logo-google.png',
            width: 30.0,
            height: 30.0,
          ),
          btnText: 'Google로 시작하기',
          btnBackgroundColor: GOOGLE_COLOR,
          textColor: WHITE_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginButton(
          onPressed: () {
            signInOAuth(oAuthType: OAuthType.naver);
          },
          isOAuth: true,
          isEnabled: true,
          btnIcon: Image.asset(
            'assets/images/logo-naver.png',
            width: 30.0,
            height: 30.0,
          ),
          btnText: 'Naver로 시작하기',
          btnBackgroundColor: NAVER_COLOR,
          textColor: WHITE_COLOR,
        ),
        SizedBox(
          height: 10.0,
        ),
        LoginButton(
          onPressed: () async {
            signInOAuth(oAuthType: OAuthType.kakao);
          },
          isOAuth: true,
          isEnabled: true,
          btnIcon: Image.asset(
            'assets/images/logo-kakao.png',
            width: 30.0,
            height: 30.0,
          ),
          btnText: 'Kakao로 시작하기',
          btnBackgroundColor: KAKAO_COLOR,
          textColor: BLACK_COLOR,
        ),
        SizedBox(
          height: 25.0,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            _ForgotText(
              forgotText: '계정이 없으신가요?',
              fontWeight: FontWeight.w300,
            ),
            SizedBox(
              width: 5.0,
            ),
            GestureDetector(
              onTap: () {
                Navigator.of(context).pushNamed('/signup');
              },
              child: _ForgotText(
                forgotText: '회원가입',
                fontWeight: FontWeight.w600,
              ),
            ),
          ],
        ),
      ],
    );
  }
}

class _ForgotText extends StatelessWidget {
  final String forgotText;
  final FontWeight fontWeight;
  const _ForgotText({
    required this.forgotText,
    required this.fontWeight,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final textStyle = TextStyle(
      color: BLACK_COLOR,
      fontSize: 15.0,
      fontWeight: fontWeight,
    );

    return Text(
      forgotText,
      style: textStyle,
      textAlign: TextAlign.center,
    );
  }
}

class _DividerLine extends StatelessWidget {
  const _DividerLine({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const SizedBox(
      width: 130,
      child: Divider(
        color: LESS_BLACK_COLOR,
        thickness: 1.0,
      ),
    );
  }
}
