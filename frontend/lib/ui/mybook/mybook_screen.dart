import 'package:flutter/material.dart';
import 'package:mybrary/ui/common/layout/default_layout.dart';

class MyBookScreen extends StatefulWidget {
  const MyBookScreen({super.key});

  @override
  State<MyBookScreen> createState() => _MyBookScreenState();
}

class _MyBookScreenState extends State<MyBookScreen> {
  @override
  Widget build(BuildContext context) {
    return DefaultLayout(
      child: Container(
        child: Center(
          child: Text('마이북'),
        ),
      ),
    );
  }
}
