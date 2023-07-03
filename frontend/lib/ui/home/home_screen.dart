import 'package:flutter/material.dart';
import 'package:mybrary/data/datasource/remote_datasource.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: Text('마이브러리 메인 화면입니다.'),
          ),
          ElevatedButton(
            onPressed: () => RemoteDataSource().getProfileData(context),
            child: Text('테스트'),
          ),
        ],
      ),
    );
  }
}
