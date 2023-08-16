import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:mybrary/res/constants/color.dart';
import 'package:mybrary/res/constants/style.dart';

class OAuthButton extends StatelessWidget {
  final String btnText;
  final String oauthType;
  final Color btnBackgroundColor;
  final GestureTapCallback? onTap;

  const OAuthButton({
    required this.btnText,
    required this.oauthType,
    required this.btnBackgroundColor,
    this.onTap,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(
          horizontal: 24.0,
          vertical: 16.0,
        ),
        decoration: BoxDecoration(
          color: btnBackgroundColor,
          borderRadius: BorderRadius.circular(4.0),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            SvgPicture.asset(
              'assets/svg/icon/$oauthType.svg',
              width: 30.0,
              height: 30.0,
            ),
            Expanded(
              child: Text(
                btnText,
                style: commonSubRegularStyle.copyWith(
                  color: oauthType == 'naver' ? commonWhiteColor : grey262626,
                  fontSize: 16.0,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
