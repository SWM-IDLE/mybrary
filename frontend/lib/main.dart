import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mybrary/screens/auth/login_screen.dart';
import 'package:mybrary/screens/auth/reset_verify_screen.dart';
import 'package:mybrary/screens/auth/sign_up_screen.dart';
import 'package:mybrary/screens/auth/sign_up_verify_screen.dart';
import 'package:mybrary/screens/home_screen.dart';

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
      initialRoute: '/login',
      routes: {
        '/login': (context) => LoginScreen(),
        '/login/verify': (context) => ResetVerifyScreen(),
        '/signup': (context) => SignUpScreen(),
        '/signup/verify': (context) => SignUpVerifyScreen(),
        '/home': (context) => HomeScreen(),
      },
    );
  }
}
