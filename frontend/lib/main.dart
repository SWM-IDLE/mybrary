import 'dart:async';

import 'package:firebase_analytics/firebase_analytics.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mybrary/provider/user_provider.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/auth/find_pw/find_password_screen.dart';
import 'package:mybrary/ui/auth/sign_in/sign_in_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_screen.dart';
import 'package:mybrary/ui/auth/sign_up/sign_up_verify_screen.dart';
import 'package:mybrary/ui/common/components/spalsh_screen.dart';
import 'package:mybrary/ui/common/layout/root_tab.dart';
import 'package:mybrary/ui/home/home_screen.dart';
import 'package:mybrary/ui/profile/profile_edit/profile_edit_screen.dart';
import 'package:mybrary/ui/search/search_isbn_scan/search_isbn_scan_screen.dart';
import 'package:mybrary/ui/search/search_screen.dart';

void main() async {
  runZonedGuarded<Future<void>>(() async {
    WidgetsFlutterBinding.ensureInitialized();
    await dotenv.load(fileName: ".env");

    await Firebase.initializeApp();

    FlutterError.onError = (errorDetails) {
      FirebaseCrashlytics.instance.recordFlutterFatalError(errorDetails);
    };

    PlatformDispatcher.instance.onError = (error, stack) {
      FirebaseCrashlytics.instance.recordError(error, stack, fatal: true);
      return true;
    };

    SystemChrome.setSystemUIOverlayStyle(
      systemLightUiOverlayStyle,
    );
    runApp(const MyApp());
  }, (error, stack) => FirebaseCrashlytics.instance.recordError(error, stack));
}

class MyApp extends StatelessWidget {
  static FirebaseAnalytics analytics = FirebaseAnalytics.instance;

  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Init.instance.initialize(context),
      builder: (BuildContext context, AsyncSnapshot snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const MaterialApp(
            home: SplashScreen(),
          );
        } else if (snapshot.hasError) {
          return const MaterialApp(
            home: Scaffold(
              body: Center(
                child: Text('앱을 실행할 수 없습니다.\n네트워크 연결 상태를 확인해주세요.'),
              ),
            ),
          );
        } else {
          return ProviderScope(
            child: MaterialApp(
              debugShowCheckedModeBanner: false,
              theme: ThemeData(
                fontFamily: 'NotoSansKR',
                splashColor: Colors.transparent,
                highlightColor: Colors.transparent,
              ),
              navigatorObservers: [
                FirebaseAnalyticsObserver(analytics: analytics)
              ],
              title: 'Mybrary',
              home: snapshot.data,
              routes: {
                '/signin': (context) => const SignInScreen(),
                '/signin/findpw': (context) => const FindPasswordScreen(),
                '/signup': (context) => const SignUpScreen(),
                '/signup/verify': (context) => const SignUpVerifyScreen(),
                '/home': (context) => const HomeScreen(),
                '/search': (context) => const SearchScreen(),
                '/search/barcode': (context) => const SearchIsbnScanScreen(),
                '/profile/edit': (context) => const ProfileEditScreen(),
              },
            ),
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
    await Future.delayed(const Duration(milliseconds: 1000));

    const secureStorage = FlutterSecureStorage();
    await UserState.init();

    final accessToken = await secureStorage.read(key: accessTokenKey);
    final refreshToken = await secureStorage.read(key: refreshTokenKey);

    if (accessToken == null || refreshToken == null) {
      return const SignInScreen();
    }

    // TODO: 초반 앱 화면에서 카메라, 앨범 권한을 획득하는 로직 필요
    return const RootTab();
  }
}
