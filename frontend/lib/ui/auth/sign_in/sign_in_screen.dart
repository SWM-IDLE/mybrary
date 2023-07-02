import 'package:flutter/material.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/colors/auth_color.dart';
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
                          SignInInput(
                            initialValue: loginPassword ?? '',
                            onSaved: (String? val) {
                              loginPassword = val;
                            },
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
                          SingInButton(
                            onPressed: () => oAuthLoginPressed(API.naverLogin),
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
                          SingInButton(
                            onPressed: () => oAuthLoginPressed(API.kakaoLogin),
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
        Navigator.of(context).pushNamed('/home');
      } else {
        print('아이디 또는 비밀번호가 틀렸습니다.');
      }
    } else {
      print('Log: 서버 에러');
    }
  }

  void oAuthLoginPressed(API api) {
    signInOAuth(getApi(api));
  }

  Future<void> signInOAuth(String api) async {
    try {
      // oAuth 로그인 페이지 요청과 동시에 redirect_url로 callback 데이터 전달
      final url = Uri.parse('$api?redirect_url=$mybraryUrlScheme');

      // mybraryUrlScheme로 전달된 callback 데이터를 await
      final result = await FlutterWebAuth.authenticate(
          url: url.toString(), callbackUrlScheme: mybraryUrlScheme);

      // AccessToken Parameter : "Authorization"
      // RefreshToken Parameter : "Authorization-Refresh"
      // callback 데이터로부터 accessToken & refreshToken 파싱
      final accessToken = Uri.parse(result).queryParameters['Authorization'];
      final refreshToken =
          Uri.parse(result).queryParameters['Authorization-Refresh'];

      print(accessToken);

      // save tokens on secure storage
      // await storage.write(key: 'ACCESS_TOKEN', value: accessToken);
      // await storage.write(key: 'REFRESH_TOKEN', value: refreshToken);
    } catch (e) {
      // showSignInFailDialog(e.toString());
    }
  }

  void showSignInFailDialog(String errMsg) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Center(
            child: Text(
          "로그인 오류",
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        )),
        content: IntrinsicHeight(
          child: Column(children: [
            Text("로그인 도중 오류가 발생하였습니다.", style: TextStyle(fontSize: 14)),
          ]),
        ),
        contentPadding: const EdgeInsets.fromLTRB(15, 15, 15, 5),
        actions: [
          Center(
            child: ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: Text('확인')),
          )
        ],
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(15),
        ),
      ),
    );
  }
}
