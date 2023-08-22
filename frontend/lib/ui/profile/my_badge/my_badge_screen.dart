import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/config.dart';
import 'package:mybrary/res/constants/style.dart';
import 'package:mybrary/ui/common/layout/subpage_layout.dart';
import 'package:mybrary/utils/logics/common_utils.dart';

class MyBadgeScreen extends StatelessWidget {
  const MyBadgeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SubPageLayout(
      appBarTitle: '마이 뱃지',
      child: CustomScrollView(
        physics: const BouncingScrollPhysics(
          parent: AlwaysScrollableScrollPhysics(),
        ),
        slivers: [
          SliverPadding(
            padding: const EdgeInsets.all(16.0),
            sliver: SliverGrid(
              delegate: SliverChildBuilderDelegate(
                (context, index) => InkWell(
                  onTap: () {
                    commonBottomSheet(
                      context: context,
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 20.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            const SizedBox(height: 4.0),
                            SvgPicture.asset(
                              badgeSvg[index].values.first,
                              width: 150.0,
                              height: 150.0,
                            ),
                            const SizedBox(height: 4.0),
                            Text(
                              badgeSvg[index].keys.first,
                              style: commonSubBoldStyle.copyWith(
                                fontSize: 21.0,
                              ),
                            ),
                            const SizedBox(height: 16.0),
                            Text(
                              badgeContent[index],
                              style: commonSubRegularStyle.copyWith(
                                color: grey555555,
                                fontSize: 16.0,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            const SizedBox(height: 16.0),
                            Container(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 32.0,
                                vertical: 8.0,
                              ),
                              decoration: BoxDecoration(
                                color: greyF1F2F5,
                                borderRadius: BorderRadius.circular(50.0),
                              ),
                              child: const Text(
                                '달성일\n2023년 8월 24일',
                                style: commonSubRegularStyle,
                                textAlign: TextAlign.center,
                              ),
                            ),
                            const SizedBox(height: 24.0),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Text(
                                  '824명',
                                  style: commonSubBoldStyle.copyWith(
                                      color: primaryColor),
                                  textAlign: TextAlign.center,
                                ),
                                const Text(
                                  '의 마이브러리 회원들이 달성',
                                  style: commonSubBoldStyle,
                                  textAlign: TextAlign.center,
                                ),
                              ],
                            ),
                            const SizedBox(height: 16.0),
                          ],
                        ),
                      ),
                    );
                  },
                  child: Column(
                    children: [
                      SvgPicture.asset(
                        badgeSvg[index].values.first,
                      ),
                      const SizedBox(height: 8.0),
                      Text(
                        badgeSvg[index].keys.first,
                        style: commonSubRegularStyle,
                      ),
                    ],
                  ),
                ),
                childCount: badgeSvg.length,
              ),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                mainAxisSpacing: 8.0,
                crossAxisSpacing: 16.0,
                childAspectRatio: 0.8,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
