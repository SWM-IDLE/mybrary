import 'package:flutter/material.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';

class MyInterestsScreen extends StatefulWidget {
  const MyInterestsScreen({super.key});

  @override
  State<MyInterestsScreen> createState() => _MyInterestsScreenState();
}

class _MyInterestsScreenState extends State<MyInterestsScreen> {
  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '마이 관심사',
      child: Center(
        child: Text('마이 관심사'),
      ),
    );
  }
}
