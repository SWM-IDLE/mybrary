import 'package:flutter/material.dart';
import 'package:mybrary/res/constants/style.dart';

class RecommendPhrase extends StatelessWidget {
  const RecommendPhrase({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Text(
              '최대 3개',
              style: commonSubRegularStyle.copyWith(
                fontWeight: FontWeight.w700,
              ),
            ),
            const Text(
              '까지 카테고리를 선택하고,',
              style: commonSubRegularStyle,
            ),
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Text(
              '관심사 별 베스트 셀러',
              style: commonSubRegularStyle.copyWith(
                fontWeight: FontWeight.w700,
              ),
            ),
            const Text(
              '를 추천받아보세요 📚',
              style: commonSubRegularStyle,
            )
          ],
        ),
      ],
    );
  }
}
