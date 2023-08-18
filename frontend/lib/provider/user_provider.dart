import 'package:shared_preferences/shared_preferences.dart';

class UserState {
  static late SharedPreferences localStorage;
  static Future init() async {
    localStorage = await SharedPreferences.getInstance();
  }

  static String get userId => localStorage.getString('userId')!;
}
