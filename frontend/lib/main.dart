import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mybrary/ui/auth/find_pw/find_password_screen.dart';
import 'package:mybrary/ui/auth/sign_in/sign_in_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_verify_screen.dart';
import 'package:mybrary/ui/home/home_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    // System Interface 글자 색상 light, dark
    // 현재는 전체 적용, 추후 일부 적용 예정
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);
    return MaterialApp(
      theme: ThemeData(
        appBarTheme: AppBarTheme(
          systemOverlayStyle: SystemUiOverlayStyle.dark,
        ),
      ),
      initialRoute: '/signin',
      routes: {
        '/signin': (context) => SignInScreen(),
        '/signin/findpw': (context) => FindPasswordScreen(),
        '/signup': (context) => SignUpScreen(),
        '/signup/verify': (context) => SignUpVerifyScreen(),
        '/home': (context) => HomeScreen(),
      },
    );
  }
}
