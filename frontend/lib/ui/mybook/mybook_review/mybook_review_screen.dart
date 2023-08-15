import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';

class MyBookReviewScreen extends StatelessWidget {
  final String thumbnailUrl;
  const MyBookReviewScreen({
    required this.thumbnailUrl,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '마이리뷰',
      child: SafeArea(
        bottom: false,
        child: SingleChildScrollView(
          physics: const BouncingScrollPhysics(),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 176,
                height: 254,
                decoration: ShapeDecoration(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                  shadows: [
                    BoxShadow(
                      color: commonBlackColor.withOpacity(0.3),
                      blurRadius: 6,
                      offset: const Offset(0, 4),
                      spreadRadius: 0,
                    )
                  ],
                  image: DecorationImage(
                    image: NetworkImage(thumbnailUrl),
                    fit: BoxFit.fill,
                  ),
                ),
              ),
              const SizedBox(height: 20.0),
            ],
          ),
        ),
      ),
    );
  }
}
