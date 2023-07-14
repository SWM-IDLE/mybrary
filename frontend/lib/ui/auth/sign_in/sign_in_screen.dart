import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/colors/color.dart';
import 'package:mybrary/res/config/config.dart';
import 'package:mybrary/ui/auth/components/logo.dart';
import 'package:mybrary/ui/auth/components/sign_in_input.dart';
import 'package:mybrary/ui/auth/components/sing_in_button.dart';
import 'package:mybrary/utils/logics/auth_regexp.dart';

const LOGIN_TEST_ID = 'test123';
const LOGIN_TEST_PASSWORD = 'test123!@';

class SignInScreen extends StatefulWidget {
  const SignInScreen({Key? key}) : super(key: key);

  @override
  State<SignInScreen> createState() => _SignInScreenState();
}

class _SignInScreenState extends State<SignInScreen> {
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
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          Text('아이디'),
                          SignInInput(
                            initialValue: loginId ?? '',
                            onSaved: (String? val) {
                              loginId = val;
                            },
                            hintText: '영문, 숫자 포함 6자 이상',
                            validator: (String? val) {
                              if (val == null || val.isEmpty) {
                                return '아이디를 입력해 주세요.';
                              }

                              if (checkAuthValidator(
                                  val, LoginRegExp.idRegExp, 6, 20)) {
                                return '아이디는 영소문자/숫자 혼합 6자 이상으로 입력해 주세요.';
                              }

                              return null;
                            },
                          ),
                          SizedBox(
                            height: 20.0,
                          ),
                          Text('비밀번호'),
                          SignInInput(
                            initialValue: loginPassword ?? '',
                            onSaved: (String? val) {
                              loginPassword = val;
                            },
                            obscureText: true,
                            hintText: '영문, 숫자, 특수문자 포함 8자 이상',
                            validator: (String? val) {
                              if (val == null || val.isEmpty) {
                                return '비밀번호를 입력해 주세요.';
                              }

                              if (checkAuthValidator(
                                  val, LoginRegExp.passwordRegExp, 8, 16)) {
                                return '비밀번호는 영문/숫자/특수문자 혼합 8자 이상으로 입력해 주세요.';
                              }

                              return null;
                            },
                          ),
                          SizedBox(
                            height: 25.0,
                          ),
                          SingInButton(
                            onPressed: onSavePressed,
                            isOAuth: false,
                            isEnabled: _isValid ?? false,
                            btnText: '로그인',
                            btnBackgroundColor: LOGIN_PRIMARY_COLOR,
                            textColor: BLACK_COLOR,
                          ),
                          SizedBox(
                            height: 25.0,
                          ),
                          GestureDetector(
                            onTap: () {
                              Navigator.of(context).pushNamed('/signin/findpw');
                            },
                            child: Text(
                              '비밀번호를 잊으셨나요?',
                              style: TextStyle(
                                color: BLACK_COLOR,
                                fontSize: 15.0,
                                fontWeight: FontWeight.w600,
                              ),
                              textAlign: TextAlign.center,
                            ),
                          ),
                        ],
                      ),
                      SizedBox(
                        height: 20.0,
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          SizedBox(
                            width: 130,
                            child: Divider(
                              color: LESS_BLACK_COLOR,
                              thickness: 1.0,
                            ),
                          ),
                          SizedBox(
                            width: 130,
                            child: Divider(
                              color: LESS_BLACK_COLOR,
                              thickness: 1.0,
                            ),
                          ),
                        ],
                      ),
                      SizedBox(
                        height: 20.0,
                      ),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          SingInButton(
                            onPressed: () => oAuthLoginPressed(API.googleLogin),
                            isOAuth: true,
                            isEnabled: true,
                            btnIcon: Image.asset(
                              'assets/logo/logo-google.png',
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
                          SingInButton(
                            onPressed: () => oAuthLoginPressed(API.naverLogin),
                            isOAuth: true,
                            isEnabled: true,
                            btnIcon: Image.asset(
                              'assets/logo/logo-naver.png',
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
                          SingInButton(
                            onPressed: () => oAuthLoginPressed(API.kakaoLogin),
                            isOAuth: true,
                            isEnabled: true,
                            btnIcon: Image.asset(
                              'assets/logo/logo-kakao.png',
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
                              Text(
                                '계정이 없으신가요?',
                                style: TextStyle(
                                  color: BLACK_COLOR,
                                  fontSize: 15.0,
                                  fontWeight: FontWeight.w300,
                                ),
                                textAlign: TextAlign.center,
                              ),
                              SizedBox(
                                width: 5.0,
                              ),
                              GestureDetector(
                                onTap: () {
                                  Navigator.of(context).pushNamed('/signup');
                                },
                                child: Text(
                                  '회원가입',
                                  style: TextStyle(
                                    color: BLACK_COLOR,
                                    fontSize: 15.0,
                                    fontWeight: FontWeight.w600,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
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

  void onSavePressed() async {
    // formKey는 생성을 했는데, Form 위젯과 결합을 안했을 때는 null
    if (formKey.currentState == null) {
      return;
    }

    if (formKey.currentState!.validate()) {
      formKey.currentState!.save();
      if (loginId == LOGIN_TEST_ID && loginPassword == LOGIN_TEST_PASSWORD) {
        Navigator.of(context)
            .pushNamedAndRemoveUntil('/home', (route) => false);
      } else {
        log('DEBUG: 테스트 계정의 아이디 또는 비밀번호가 틀렸습니다.');
      }
    } else {
      log('ERROR: 서버 에러가 발생했습니다.');
    }
  }

  void oAuthLoginPressed(API api) {
    signInOAuth(getApi(api));
  }

  Future<void> signInOAuth(String api) async {
    const secureStorage = FlutterSecureStorage();
    try {
      // oAuth 로그인 페이지 요청과 동시에 redirect_url로 callback 데이터 전달
      final url = Uri.parse('$api?redirect_url=$mybraryUrlScheme');

      // mybraryUrlScheme로 전달된 callback 데이터를 await
      final result = await FlutterWebAuth.authenticate(
          url: url.toString(), callbackUrlScheme: mybraryUrlScheme);

      // AccessToken Parameter : "Authorization"
      // RefreshToken Parameter : "Authorization-Refresh"
      // callback 데이터로부터 accessToken & refreshToken 파싱
      final accessToken =
          Uri.parse(result).queryParameters[accessTokenHeaderKey];
      final refreshToken =
          Uri.parse(result).queryParameters[refreshTokenHeaderKey];

      // secureStorage에 accessToken & refreshToken 저장
      await secureStorage.write(key: accessTokenKey, value: accessToken);
      await secureStorage.write(key: refreshTokenKey, value: refreshToken);
    } catch (e) {
      showSignInFailDialog(e.toString());
    }

    // 로그인 성공 시 홈 화면으로 이동
    Navigator.of(context)
        .pushNamedAndRemoveUntil('/home', (Route<dynamic> route) => false);
  }

  void showSignInFailDialog(String errMessage) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Center(
          child: Text(
            "마이브러리",
            style: TextStyle(
              fontWeight: FontWeight.w700,
              fontSize: 20,
            ),
          ),
        ),
        content: IntrinsicHeight(
          child: Wrap(
            alignment: WrapAlignment.center,
            children: [
              Text(
                "잠시 후 다시 시도해주세요.",
                style: TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w700,
                ),
              ),
              SizedBox(
                height: 5.0,
              ),
              Text(
                "로그인 중에 오류가 발생하였습니다.",
                style: TextStyle(fontSize: 14),
              ),
              Text(
                "지속적으로 발생할 경우, 고객센터로 문의해주세요.",
                style: TextStyle(fontSize: 14),
              ),
            ],
          ),
        ),
        contentPadding: const EdgeInsets.all(10),
        actions: [
          Center(
            child: ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text('확인'),
            ),
          ),
        ],
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(15),
        ),
      ),
    );
  }
}
