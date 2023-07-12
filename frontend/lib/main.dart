import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/res/config/config.dart';
import 'package:mybrary/ui/auth/find_pw/find_password_screen.dart';
import 'package:mybrary/ui/auth/sign_in/sign_in_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_verify_screen.dart';
import 'package:mybrary/ui/home/home_screen.dart';
import 'package:mybrary/ui/search/search_barcode_scan/search_barcode_scan_screen.dart';
import 'package:mybrary/ui/search/search_screen.dart';

void main() {
  // System Interface 글자 색상 light, dark
  // 현재는 전체 적용, 추후 일부 적용 예정
  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Init.instance.initialize(context),
      builder: (BuildContext context, AsyncSnapshot snapshot) {
        // snapshot 여부에 따른 앱 로딩 화면 또는 에러 화면
        // 추후 마이브러리 로고로 대체 될 예정
        if (snapshot.connectionState == ConnectionState.waiting) {
          return MaterialApp(
            home: Scaffold(
              body: Center(
                child: CircularProgressIndicator(),
              ),
            ),
          );
        } else if (snapshot.hasError) {
          return MaterialApp(
            home: Scaffold(
              body: Center(
                child: Text('앱을 실행할 수 없습니다.\n네트워크 연결 상태를 확인해주세요.'),
              ),
            ),
          );
        } else {
          return MaterialApp(
            debugShowCheckedModeBanner: false,
            title: 'Mybrary',
            home: snapshot.data,
            initialRoute: '/signin',
            routes: {
              '/signin': (context) => SignInScreen(),
              '/signin/findpw': (context) => FindPasswordScreen(),
              '/signup': (context) => SignUpScreen(),
              '/signup/verify': (context) => SignUpVerifyScreen(),
              '/home': (context) => HomeScreen(),
              '/search': (context) => SearchScreen(),
              '/search/barcode': (context) => SearchBarcodeScanScreen(),
            },
          );
        }
      },
    );
  }
}

class Init {
  Init._();
  static final instance = Init._();

  Future<Widget?> initialize(BuildContext context) async {
    await Future.delayed(Duration(milliseconds: 1000));

    const secureStorage = FlutterSecureStorage();

    final accessToken = await secureStorage.read(key: accessTokenKey);
    final refreshToken = await secureStorage.read(key: refreshTokenKey);

    // accessToken과 refreshToken이 없으면 로그인 화면으로 이동
    if (accessToken == null || refreshToken == null) return SearchScreen();

    // 토큰이 존재하면, 홈 화면으로 바로 이동
    return HomeScreen();
  }
}
