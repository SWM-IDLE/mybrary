import 'package:flutter/material.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';

class MyBadgeScreen extends StatelessWidget {
  const MyBadgeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '마이 뱃지',
      child: Center(
        child: Text('마이 뱃지'),
      ),
    );
  }
}
