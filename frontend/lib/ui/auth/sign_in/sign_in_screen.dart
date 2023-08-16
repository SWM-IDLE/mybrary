import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';
import 'package:mybrary/data/network/api.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/ui/auth/components/logo.dart';
import 'package:mybrary/ui/auth/components/oauth_button.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';

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
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          image: AssetImage('assets/img/logo/login_background.jpg'),
          fit: BoxFit.cover,
        ),
      ),
      child: DefaultLayout(
        backgroundColor: Colors.transparent,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Align(
                alignment: Alignment.topLeft,
                child: Logo(
                  logoText: '도서의 가치를 발견할\n당신만의 도서\n마이브러리',
                ),
              ),
              Column(
                children: [
                  const SizedBox(height: 20.0),
                  Align(
                    alignment: Alignment.bottomCenter,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        OAuthButton(
                          btnText: 'Google로 시작하기',
                          oauthType: 'google',
                          btnBackgroundColor: greyF1F2F5,
                          onTap: () => oAuthLoginPressed(API.googleLogin),
                        ),
                        const SizedBox(height: 10.0),
                        OAuthButton(
                          btnText: '네이버로 시작하기',
                          oauthType: 'naver',
                          btnBackgroundColor: naverLoginColor,
                          onTap: () => oAuthLoginPressed(API.naverLogin),
                        ),
                        const SizedBox(height: 10.0),
                        OAuthButton(
                          btnText: '카카오로 시작하기',
                          oauthType: 'kakao',
                          btnBackgroundColor: kakaoLoginColor,
                          onTap: () => oAuthLoginPressed(API.kakaoLogin),
                        ),
                        const SizedBox(height: 70.0),
                      ],
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
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
